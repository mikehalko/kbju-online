package ru.mikehalko.kbju.util.web.validation;


import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.OtherAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserAttribute;

import static ru.mikehalko.kbju.web.constant.attribute.UserAttribute.*;

public class UserValidation implements Validation {
    private static final String SEPARATOR = ";\n";
    public static final String MESSAGE_MUST_BE_HIGHER = "%s must be higher %s"; // TODO перевод
    public static final String MESSAGE_MUST_BE_LOWER = "%s must be lower %s";

    // TODO задействовать существующий enum
    public final boolean USER_ID_EMPTY = false;
    public final boolean NAME_EMPTY = false;
    public final boolean CALORIES_MIN_EMPTY = false;
    public final boolean CALORIES_MAX_EMPTY = false;

    public final boolean CALORIES_MIN_HIGHER_MAX = false; // !


    public final int USER_ID_MIN = 0;
    public final int NAME_LENGTH_MIN = 3;
    public final int NAME_LENGTH_MAX = 15;
    public final int CALORIES_MIN_MIN = 0;
    public final int CALORIES_MIN_MAX = 10000;
    public final int CALORIES_MAX_MIN = 0;
    public final int CALORIES_MAX_MAX = 10000;

    private StringBuilder message;
    private boolean valid = true;
    private final FailFields failFields = new FailFields();

    @Override
    public Constant attribute() {
        return OtherAttribute.VALIDATOR_USER;
    }
    @Override
    public void catchEx(String field, Exception exception) {
        createMessageIfNull();
        message.append(exception.getClass().getName()).append(" for field ").append(field).append(SEPARATOR);
    }

    public void id(int id) {
        if (id < USER_ID_MIN) {
            invalid(PARAM_USER_ID);
            appendMustBeHigher(PARAM_USER_ID.value(), id);
        }
    }

    public void name(String name) {
        if (name.length() < NAME_LENGTH_MIN) {
            invalid(PARAM_NAME);
            appendMustBeHigher(PARAM_NAME.value(), name);
        } else if (name.length() > NAME_LENGTH_MAX) {
            invalid(PARAM_NAME);
            appendMustBeLower(PARAM_NAME.value(), name);
        }
    }

    public void calories(int min, int max) {
        if (min < CALORIES_MIN_MIN) {
            invalid(PARAM_CALORIES_MIN);
            appendMustBeHigher(PARAM_CALORIES_MIN.value(), min);
        } else if (min > CALORIES_MIN_MAX) {
            invalid(PARAM_CALORIES_MIN);
            appendMustBeLower(PARAM_CALORIES_MIN.value(), min);
        }

        if (max < CALORIES_MAX_MIN) {
            invalid(PARAM_CALORIES_MAX);
            appendMustBeHigher(PARAM_CALORIES_MAX.value(), max);
        } else if (max > CALORIES_MAX_MAX) {
            invalid(PARAM_CALORIES_MAX);
            appendMustBeLower(PARAM_CALORIES_MAX.value(), max);
        }

        if (!CALORIES_MIN_HIGHER_MAX && min > max) {
            invalid(PARAM_CALORIES_MIN);
            invalid(PARAM_CALORIES_MAX);
            appendWithSeparator(String.format("%s cant be higher than %s", PARAM_CALORIES_MIN, PARAM_CALORIES_MAX));
        }
    }

    public void invalid(UserAttribute field) {
        createMessageIfNull();
        invalid();
        failFields.invalid(field);
    }

    public void invalid() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public boolean isValid(UserAttribute field) {
        return failFields.isValid(field);
    }

    public boolean isNoValid(UserAttribute field) {
        return failFields.isNoValid(field);
    }


    public String resultMessage() {
        return message != null ? message.toString() : "";
    }

    private void createMessageIfNull() {
        message = message == null ? new StringBuilder() : message;
    }


    private void appendMustBeHigher(String param, Object value) {
        appendWithSeparator(String.format(MESSAGE_MUST_BE_HIGHER, param, value));
    }

    private void appendMustBeLower(String param, Object value) {
        appendWithSeparator(String.format(MESSAGE_MUST_BE_LOWER, param, value));
    }

    private void appendWithSeparator(String text) {
        message.append(text).append(SEPARATOR);
    }
}
