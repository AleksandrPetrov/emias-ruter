package ru.emias.router.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import ru.emias.router.domain.entity.EOperationType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({"id", "parameterId", "any", "operationType", "values"})
public class RuleParameterValueDto {

    private Integer id;

    @NotNull
    @JsonProperty("parameterName")
    private String parameterName;

    @NotNull
    @JsonProperty("any")
    private boolean any;

    @NotNull
    @JsonProperty("operationType")
    private EOperationType operationType;

    @JsonProperty("values")
    private List<String> values = new ArrayList<String>();

    public RuleParameterValueDto() {
        super();
    }

    public RuleParameterValueDto(String parameterName, boolean any, EOperationType operationType, String... values) {
        super();
        this.parameterName = parameterName;
        this.any = any;
        this.operationType = operationType;
        this.values.addAll(Arrays.asList(values));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
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
