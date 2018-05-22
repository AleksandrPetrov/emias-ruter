package ru.emias.router.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import ru.emias.router.domain.entity.EParameterType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonInclude(value = Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "parameterType", "description", "rank"})
public class ParameterDto {

    @JsonProperty("id")
    private Integer id;

    @NotNull
    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("parameterType")
    private EParameterType parameterType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("rank")
    private Integer rank;

    public ParameterDto() {
        super();
    }

    public ParameterDto(String name, EParameterType parameterType, String description, Integer rank) {
        super();
        this.name = name;
        this.parameterType = parameterType;
        this.description = description;
        this.rank = rank;
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

    public EParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(EParameterType parameterType) {
        this.parameterType = parameterType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

}
