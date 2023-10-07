package ru.mikehalko.kbju.web.constant.attribute;

public enum MealField implements FieldAttribute {

    PARAM_ID("id"),
    PARAM_DATE_TIME("dateTime"),
    PARAM_DESCRIPTION("description"),
    PARAM_MASS("mass"),
    PARAM_PROTEINS("proteins"),
    PARAM_FATS("fats"),
    PARAM_CARBOHYDRATES("carbohydrates"),
    PARAM_CALORIES("calories");

    private final String attributeValue;

    private MealField(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String value() {
        return attributeValue;
    }

    @Override
    public Field fieldType() {
        return Field.MEAL;
    }

    @Override
    public String toString() {
        return attributeValue;
    }
}
