package ru.mikehalko.kbju.web.validation;

import ru.mikehalko.kbju.web.validation.exception.WrongTypeEnumFieldException;
import ru.mikehalko.kbju.web.constant.attribute.*;

public class FailFields {
    private byte[] mealValidationArray = null;
    private byte[] userValidationArray = null;
    private byte[] userCredentialValidationArray = null;


    public <T extends FieldAttribute> void invalid(T field) {
        switch (field.fieldType()) {
            case USER:
                prepareUserArray();
                invalid((UserField) field);
                break;
            case MEAL:
                prepareMealArray();
                invalid((MealField) field);
                break;
            case USER_CREDENTIAL:
                prepareUserCredentialArray();
                invalid((UserCredentialField) field);
                break;
            default:
                throw new WrongTypeEnumFieldException(field.getClass());
        }
    }

    public <T extends FieldAttribute> boolean isValid(T field) {
        switch (field.fieldType()) {
            case USER:
                prepareUserArray();
                return isValid((UserField) field);
            case MEAL:
                prepareMealArray();
                return isValid((MealField) field);
            case USER_CREDENTIAL:
                prepareUserCredentialArray();
                return isValid((UserCredentialField) field);
        }
        throw new WrongTypeEnumFieldException(field.getClass());
    }

    public <T extends FieldAttribute> boolean isNoValid(T field) {
        return !isValid(field);
    }

    private void prepareMealArray() {
        mealValidationArray = mealValidationArray == null ? new byte[MealField.values().length] : mealValidationArray;
    }
    private void prepareUserArray() {
        userValidationArray = userValidationArray == null ? new byte[UserField.values().length] : userValidationArray;
    }
    private void prepareUserCredentialArray() {
        userCredentialValidationArray = userCredentialValidationArray == null ? new byte[UserCredentialField.values().length] : userCredentialValidationArray;
    }

    private void invalid(MealField field) {
        mealValidationArray[field.ordinal()] = 1;
    }
    private boolean isValid(MealField field) {
        return mealValidationArray[field.ordinal()] == 0;
    }

    private void invalid(UserField field) {
        userValidationArray[field.ordinal()] = 1;
    }
    private boolean isValid(UserField field) {
        return userValidationArray[field.ordinal()] == 0;
    }

    private void invalid(UserCredentialField field) {
        userCredentialValidationArray[field.ordinal()] = 1;
    }
    private boolean isValid(UserCredentialField field) {
        return userCredentialValidationArray[field.ordinal()] == 0;
    }
}
