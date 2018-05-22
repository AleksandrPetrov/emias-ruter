package ru.emias.router.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "targetUrl", "parameterValues"})
public class RuleDto {

    @JsonProperty("id")
    private Integer id;

    @NotNull
    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotNull
    @NotEmpty
    @JsonProperty("targetUrl")
    private String targetUrl;

    @JsonProperty("parameterValues")
    private List<RuleParameterValueDto> parameterValues = new ArrayList<RuleParameterValueDto>();

    public RuleDto() {
        super();
    }

    public RuleDto(String name, String targetUrl) {
        super();
        this.name = name;
        this.targetUrl = targetUrl;
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

    public List<RuleParameterValueDto> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(List<RuleParameterValueDto> parameterValues) {
        this.parameterValues = parameterValues;
    }

}
