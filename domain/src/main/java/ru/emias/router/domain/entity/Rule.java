package ru.emias.router.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private Integer id;

    private String name;

    private String targetUrl;

    private List<RuleParameterValue> parameterValues = new ArrayList<RuleParameterValue>();

    public Rule() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public RuleParameterValue getParameterValue(Parameter p) {
        for (RuleParameterValue ruleParameterValue : parameterValues) {
            if (ruleParameterValue.getParameter().getId().equals(p.getId())) return ruleParameterValue;
        }
        return null;
    }

    public List<RuleParameterValue> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(List<RuleParameterValue> parameterValues) {
        this.parameterValues = parameterValues;
    }

}
