package ru.emias.router.domain.repository;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ru.emias.router.domain.entity.Parameter;
import ru.emias.router.domain.exception.ExceptionCodes;
import ru.emias.router.domain.exception.RouterException;

@Repository
@SuppressWarnings("unchecked")
public class ParameterRepository extends AbstractRepository {

    public List<Parameter> findAll() {
        Criteria criteria = getHibernateSession().createCriteria(Parameter.class);
        criteria.addOrder(Order.asc("rank").nulls(NullPrecedence.LAST));
        criteria.addOrder(Order.asc("id"));
        return Collections.unmodifiableList(criteria.list());
    }

    public Parameter findById(Integer id) {
        return getHibernateSession().get(Parameter.class, id);
    }

    public Parameter findByName(String name) {
        Criteria criteria = getHibernateSession().createCriteria(Parameter.class).add(Restrictions.eq("name", name));
        Parameter entity = (Parameter) criteria.uniqueResult();

        if (entity != null) getHibernateSession().merge(entity);
        return entity;
    }

    public void save(Parameter parameter) throws RouterException {
        // em.detach(parameter);
        // проверка на дубли
        Parameter parameterWithSameName = findByName(parameter.getName());
        if (parameterWithSameName != null && !parameterWithSameName.getId().equals(parameter.getId()))
            throw new RouterException(ExceptionCodes.PARAMETER_NAME_IS_ALREADY_USED, String.format("Обнаружен дубликат параметра: %s", parameter.getName()));

        if (parameter.getId() == null)
            getHibernateSession().save(parameter);
        else
            getHibernateSession().merge(parameter);
    }

    public void remove(int id) throws RouterException {
        Parameter parameterToDelete = getHibernateSession().load(Parameter.class, id);
        remove(parameterToDelete);
    }

    public void remove(Parameter parameter) throws RouterException {
        if (parameter == null)
            throw new RouterException(ExceptionCodes.UNKNOWN_EXCEPTION, "Непредвиденная ошибка. Параметр уже удалён.");
        getHibernateSession().delete(parameter);
    }

}