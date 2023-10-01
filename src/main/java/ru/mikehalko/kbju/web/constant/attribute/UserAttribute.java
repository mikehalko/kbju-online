package ru.mikehalko.kbju.web.constant.attribute;

import ru.mikehalko.kbju.web.constant.Constant;

public enum UserAttribute implements Constant {

    PARAM_USER_ID("user_id"),
    PARAM_NAME("name"),
    PARAM_CALORIES_MIN("calories_min"),
    PARAM_CALORIES_MAX("calories_max");

    private final String attributeValue;

    private UserAttribute(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String value() {
        return attributeValue;
    }

    @Override
    public String toString() {
        return attributeValue;
    }
}
