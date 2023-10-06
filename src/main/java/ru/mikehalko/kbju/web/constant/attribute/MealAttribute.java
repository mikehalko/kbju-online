package ru.mikehalko.kbju.web.constant.attribute;

import ru.mikehalko.kbju.web.constant.Constant;

public enum MealAttribute implements Constant {

    PARAM_ID("id"),
    PARAM_DATE_TIME("dateTime"),
    PARAM_DESCRIPTION("description"),
    PARAM_MASS("mass"),
    PARAM_PROTEINS("proteins"),
    PARAM_FATS("fats"),
    PARAM_CARBOHYDRATES("carbohydrates"),
    PARAM_CALORIES("calories");

    private final String attributeValue;

    private MealAttribute(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String value() {
        return attributeValue;
    }

    // TODO перевод значения метод  

    @Override
    public String toString() {
        return attributeValue;
    }
}
