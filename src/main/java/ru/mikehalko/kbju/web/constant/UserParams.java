package ru.mikehalko.kbju.web.constant;

public enum UserParams implements Params { // TODO переименовать в атрибут?

    PARAM_USER_ID("user_id"),
    PARAM_NAME("name"),
    PARAM_CALORIES_MIN("calories_min"),
    PARAM_CALORIES_MAX("calories_max");

    private final String paramValue;

    private UserParams(String paramValue) {
        this.paramValue = paramValue;
    }

    public String value() {
        return paramValue;
    }

    @Override
    public String toString() {
        return paramValue;
    }
}
