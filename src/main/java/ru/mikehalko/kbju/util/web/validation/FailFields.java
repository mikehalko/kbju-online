package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.MealParams;
import ru.mikehalko.kbju.web.constant.UserParams;

public class FailFields {
    private final byte[] mealValidationArray = new byte[MealParams.values().length];
    private final byte[] userValidationArray = new byte[UserParams.values().length];

    public void invalid(MealParams field) {
        mealValidationArray[field.ordinal()] = 1;
    }
    public boolean isValid(MealParams field) {
        return mealValidationArray[field.ordinal()] == 0;
    }
    public boolean isNoValid(MealParams field) {
        return !isValid(field);
    }

    public void invalid(UserParams field) {
        userValidationArray[field.ordinal()] = 1;
    }
    public boolean isValid(UserParams field) {
        return userValidationArray[field.ordinal()] == 0;
    }
    public boolean isNoValid(UserParams field) {
        return !isValid(field);
    }
}
