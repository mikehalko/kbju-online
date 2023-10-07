package ru.mikehalko.kbju.web.constant.attribute;

public enum UserField implements FieldAttribute {

    PARAM_USER_ID("user_id"),
    PARAM_NAME("name"),
    PARAM_CALORIES_MIN("calories_min"),
    PARAM_CALORIES_MAX("calories_max");

    private final String attributeValue;

    private UserField(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String value() {
        return attributeValue;
    }

    @Override
    public Field fieldType() {
        return Field.USER;
    }

    @Override
    public String toString() {
        return attributeValue;
    }
}
