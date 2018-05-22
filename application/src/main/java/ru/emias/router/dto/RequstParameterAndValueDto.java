package ru.emias.router.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({"name", "value"})
public class RequstParameterAndValueDto {

    @NotNull
    @JsonProperty("name")
    private String paramaterName;

    @NotNull
    @JsonProperty("value")
    private String value;

    public RequstParameterAndValueDto() {
        super();
    }

    public RequstParameterAndValueDto(String paramaterName, String value) {
        super();
        this.paramaterName = paramaterName;
        this.value = value;
    }

    public String getParamaterName() {
        return paramaterName;
    }

    public void setParamaterName(String paramaterName) {
        this.paramaterName = paramaterName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
