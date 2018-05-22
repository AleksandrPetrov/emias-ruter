package ru.emias.router.domain.entity;

public class Parameter {

    private Integer id;

    // Наименование параметра, т.е. имя которое должно передать на вход
    // маршрутизатору клиентское приложение
    private String name;

    // Тип – тип данных параметры
    private EParameterType parameterType;

    // Описание – текстовое описание параметра
    private String description;

    // Приоритет (rank) – то в каком порядке параметр будет выставлен (обработан
    // в правиле)
    private Integer rank;

    public Parameter() {
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
