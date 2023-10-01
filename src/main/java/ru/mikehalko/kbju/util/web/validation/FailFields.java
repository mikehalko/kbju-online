package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.attribute.MealAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserAttribute;

public class FailFields {
    private final byte[] mealValidationArray = new byte[MealAttribute.values().length];
    private final byte[] userValidationArray = new byte[UserAttribute.values().length];
    private final byte[] userCredentialValidationArray = new byte[UserCredentialAttribute.values().length];

    public void invalid(MealAttribute field) {
        mealValidationArray[field.ordinal()] = 1;
    }
    public boolean isValid(MealAttribute field) {
        return mealValidationArray[field.ordinal()] == 0;
    }
    public boolean isNoValid(MealAttribute field) {
        return !isValid(field);
    }

    public void invalid(UserAttribute field) {
        userValidationArray[field.ordinal()] = 1;
    }
    public boolean isValid(UserAttribute field) {
        return userValidationArray[field.ordinal()] == 0;
    }
    public boolean isNoValid(UserAttribute field) {
        return !isValid(field);
    }

    public void invalid(UserCredentialAttribute field) {
        userCredentialValidationArray[field.ordinal()] = 1;
    }
    public boolean isValid(UserCredentialAttribute field) {
        return userCredentialValidationArray[field.ordinal()] == 0;
    }
    public boolean isNoValid(UserCredentialAttribute field) {
        return !isValid(field);
    }
}
