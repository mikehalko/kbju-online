package ru.mikehalko.kbju.web.constant.attribute;

import ru.mikehalko.kbju.web.constant.Constant;

public enum OtherAttribute implements Constant {

    USER("user"),
    USER_EDIT("user_edit"),
    MEAL("meal"),
    CREDENTIAL("credential"),
    ATTRIBUTE_MEALS_LIST("list"),

    VALIDATOR_MEAL("validator_meal"),
    VALIDATOR_USER("validator_user"),
    VALIDATOR_USER_CREDENTIAL("validator_user_credential");

    private final String attributeValue;

    private OtherAttribute(String attributeValue) {
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
