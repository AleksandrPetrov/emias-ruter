package ru.emias.router.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class RuleParameterValue {

    private Integer id;

    private Rule rule;

    private Parameter parameter;

    private boolean any;

    private EOperationType operationType;

    private List<String> values = new ArrayList<String>();

    public RuleParameterValue() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public boolean isAny() {
        return any;
    }

    public void setAny(boolean any) {
        this.any = any;
    }

    public EOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(EOperationType operationType) {
        this.operationType = operationType;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

}
