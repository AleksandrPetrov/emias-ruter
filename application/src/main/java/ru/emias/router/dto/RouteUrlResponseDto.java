package ru.emias.router.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({"targetUrl", "values"})
public class RouteUrlResponseDto {

    @JsonProperty("targetUrl")
    private String targetUrl;

    @JsonProperty("values")
    private List<RequstParameterAndValueDto> values = new ArrayList<RequstParameterAndValueDto>();

    public RouteUrlResponseDto() {
        super();
    }

    public RouteUrlResponseDto(List<RequstParameterAndValueDto> values, String targetUrl) {
        super();
        this.values = values;
        this.targetUrl = targetUrl;
    }

    public List<RequstParameterAndValueDto> getValues() {
        return values;
    }

    public void setValues(List<RequstParameterAndValueDto> values) {
        this.values = values;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

}
