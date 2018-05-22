package ru.emias.router.domain.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.emias.router.domain.entity.EOperationType;
import ru.emias.router.domain.entity.Parameter;
import ru.emias.router.domain.entity.Rule;
import ru.emias.router.domain.entity.RuleParameterValue;
import ru.emias.router.domain.exception.ExceptionCodes;
import ru.emias.router.domain.exception.RouterException;

@Repository
@SuppressWarnings("unchecked")
public class RuleRepository extends AbstractRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Rule> findAll() {
        Criteria criteria = getHibernateSession().createCriteria(Rule.class);
        criteria.addOrder(Order.asc("id"));
        return Collections.unmodifiableList(criteria.list());
    }

    public Rule findById(Integer id) {
        return getHibernateSession().get(Rule.class, id);
    }

    public void save(Rule rule) throws RouterException {
        for (RuleParameterValue pv : rule.getParameterValues()) {
            if (pv.isAny()) { // чистим поле со значениями если Any был выбран
                pv.setOperationType(EOperationType.EQ);
                pv.getValues().clear();
            }
            else {
                if (pv.getValues().isEmpty())
                    throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, //
                    String.format("Непредвиденная ошибка. Не задано ни одного значения для параметра %s.", pv.getParameter().getName()));
                for (String value : pv.getValues()) {
                    if (!pv.getParameter().getParameterType().checkValueOfCurrentType(value)) {
                        throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, //
                        String.format("Непредвиденная ошибка. Значение %s указанное для параметра %s не сотвествуют его типу %s.", value, pv.getParameter().getName(), pv.getParameter().getParameterType()));
                    }
                }
            }
        }

        validateOnSameParameters(rule);
        validateOnRulesWithSameValues(rule);

        deleteRuleParameters(rule.getId());
        if (rule.getId() == null)
            getHibernateSession().save(rule);
        else
            getHibernateSession().merge(rule);
        getHibernateSession().flush();
    }

    private void deleteRuleParameters(Integer ruleId) throws RouterException {
        if (ruleId != null) {
            Rule oldRule = findById(ruleId);
            if (oldRule != null) for (RuleParameterValue pv : oldRule.getParameterValues()) {
                getHibernateSession().delete(pv);
            }
            oldRule.getParameterValues().clear();
        }
        getHibernateSession().flush();
    }

    private void validateOnSameParameters(Rule rule) throws RouterException {
        Set<Integer> paramIdSet = new HashSet<Integer>();
        for (RuleParameterValue paramValue : rule.getParameterValues()) {
            if (!paramIdSet.add(paramValue.getParameter().getId()))
                throw new RouterException(ExceptionCodes.TWO_SAME_PARAMETERS_IN_ONE_RULE, String.format("В правиле уже задан параметр %s.", paramValue.getParameter().getName()));
        }
    }

    private void validateOnRulesWithSameValues(Rule rule) throws RouterException {
        // в двух разных правилах для одного параметра указаны одни и те же
        // частные значения,
        // при этом нет ключа параметр\значение, который мог бы обеспечить
        // уникальность одного из правил.
        Map<Integer, List<String>> mapParamIdToValues = new HashMap<Integer, List<String>>();

        if (rule.getParameterValues().size() > 0) {
            Criteria criteriaRulesWithSameValues = getHibernateSession().createCriteria(Rule.class, "r");
            if (rule.getId() != null) criteriaRulesWithSameValues.add(Restrictions.ne("r.id", rule.getId()));
            for (RuleParameterValue pv : rule.getParameterValues()) {
                DetachedCriteria dc = DetachedCriteria.forClass(RuleParameterValue.class, "pv");
                dc.setProjection(Projections.id());
                dc.add(Restrictions.eqProperty("pv.rule.id", "r.id"));
                dc.add(Restrictions.eq("pv.parameter", pv.getParameter()));
                dc.add(Restrictions.eq("pv.any", pv.isAny()));
                dc.add(Restrictions.eq("pv.operationType", pv.getOperationType()));

                criteriaRulesWithSameValues.add(Subqueries.exists(dc));

                mapParamIdToValues.put(pv.getParameter().getId(), pv.getValues());
            }

            List<Rule> possibleDoubles = criteriaRulesWithSameValues.list();
            for (Rule ruleToCompareTo : possibleDoubles) {
                boolean same = true;
                for (RuleParameterValue pvToCompare : ruleToCompareTo.getParameterValues()) {
                    List<String> originalValues = mapParamIdToValues.get(pvToCompare.getParameter().getId());
                    same = same && pvToCompare.getValues().containsAll(originalValues)
                            && originalValues.containsAll(pvToCompare.getValues());
                }

                if (same) {
                    logger.info(String.format("Конфликт правил. Правило с такими значениями уже есть: %s", ruleToCompareTo.getName()));
                    throw new RouterException(ExceptionCodes.RULES_WITH_SAME_VALUES, //
                    String.format("Конфликт правил. %s с таким значением уже задан в одном из правил.", rule.getParameterValues().get(0).getParameter().getName()));
                }
            }

        }
    }

    public void remove(int id) throws RouterException {
        Rule ruleToDelete = findById(id);
        remove(ruleToDelete);
    }

    public void remove(Rule rule) throws RouterException {
        if (rule == null)
            throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, "Непредвиденная ошибка. Правило уже удалёно.");
        getHibernateSession().delete(rule);
    }

    public Boolean checkIfParameterIsUsedInRules(Integer parameterId) {
        if (parameterId == null) return false;
        Criteria criteria = getHibernateSession().createCriteria(RuleParameterValue.class).add(Restrictions.eq("parameter.id", parameterId)).setProjection(Projections.count("id"));
        Number numberOfLinkedRules = (Number) criteria.uniqueResult();
        return numberOfLinkedRules.intValue() > 0;
    }

    public Rule findRuleForSpecifiedValues(Map<Parameter, String> mapParameterToValue) {
        if (mapParameterToValue.isEmpty()) return null;

        Criteria criteriaRulesWithParamsFromRequest = getHibernateSession().createCriteria(Rule.class, "r");
        for (Entry<Parameter, String> paramToValue : mapParameterToValue.entrySet()) {
            DetachedCriteria dc = DetachedCriteria.forClass(RuleParameterValue.class, "pv");
            dc.setProjection(Projections.id());
            dc.add(Restrictions.eqProperty("pv.rule.id", "r.id"));
            dc.add(Restrictions.eq("pv.parameter", paramToValue.getKey()));
            criteriaRulesWithParamsFromRequest.add(Subqueries.exists(dc));
        }
        criteriaRulesWithParamsFromRequest.addOrder(Order.asc("r.id"));
        List<Rule> rules = criteriaRulesWithParamsFromRequest.list();

        // сортируем так чтобы правила с параметрами=Any оказались в конце
        Collections.sort(rules, new Comparator<Rule>() {
            public int compare(Rule r1, Rule r2) {
                for (Parameter param : mapParameterToValue.keySet()) {
                    boolean r1Any = r1.getParameterValue(param).isAny();
                    boolean r2Any = r2.getParameterValue(param).isAny();
                    if (r1Any != r2Any) return r1Any ? 1 : -1;
                }
                return r1.getId().compareTo(r2.getId());
            }
        });

        for (Rule rule : rules)
            if (ruleMatchesValues(rule, mapParameterToValue)) return rule;

        return null;
    }

    private boolean ruleMatchesValues(Rule r, Map<Parameter, String> mapParameterToValue) {
        for (Entry<Parameter, String> entry : mapParameterToValue.entrySet()) {
            RuleParameterValue pv = r.getParameterValue(entry.getKey());
            if (!pv.isAny()) {
                if (pv.getOperationType() == EOperationType.EQ && !pv.getValues().contains(entry.getValue()))
                    return false;
                if (pv.getOperationType() == EOperationType.NE && pv.getValues().contains(entry.getValue()))
                    return false;
            }
        }
        return mapParameterToValue.size() == r.getParameterValues().size();
    }
}
