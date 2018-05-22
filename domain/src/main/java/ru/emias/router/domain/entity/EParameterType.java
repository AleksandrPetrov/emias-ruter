package ru.emias.router.domain.entity;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum EParameterType {
    // текст (Рус, Англ, возможны спец. символы),
    TEXT,
    // логический (true\false),
    BOOLEAN,
    // числовой (целые числа)
    NUMERIC;

    public boolean checkValueOfCurrentType(String value) {
        switch (this) {
            case BOOLEAN:
                return Arrays.asList("true", "false").contains(value);
            case NUMERIC:
                Pattern pattern = Pattern.compile("-{0,1}[0-9]+");
                return pattern.matcher(value).matches();
            default:
                return true;
        }

    }
}
