package ru.mikehalko.kbju.web.constant;

public enum MealParams implements Params {

    PARAM_ID("id"),
    PARAM_DATE_TIME("dateTime"),
    PARAM_DESCRIPTION("description"),
    PARAM_MASS("mass"),
    PARAM_PROTEINS("proteins"),
    PARAM_FATS("fats"),
    PARAM_CARBOHYDRATES("carbohydrates"),
    PARAM_CALORIES("calories");

    private final String paramValue;

    private MealParams(String paramValue) {
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
