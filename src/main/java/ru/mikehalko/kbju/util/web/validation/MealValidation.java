package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.MealAttribute;
import ru.mikehalko.kbju.web.constant.attribute.OtherAttribute;

import static ru.mikehalko.kbju.web.constant.attribute.MealAttribute.*;

import java.time.LocalDateTime;

public class MealValidation implements Validation {
    private static final String SEPARATOR = ";\n";
    public static final String MESSAGE_MUST_BE_HIGHER = "%s must be higher %s";
    public static final String MESSAGE_MUST_BE_LOWER = "%s must be lower %s";

    public final boolean ID_EMPTY = false;
    public final boolean DATETIME_EMPTY = false;
    public final boolean DESCRIPTION_EMPTY = false;
    public final boolean MASS_EMPTY = false;
    public final boolean PROTEINS_EMPTY = false;
    public final boolean FATS_EMPTY = false;
    public final boolean CARBOHYDRATES_EMPTY = false;
    public final boolean CALORIES_EMPTY = false;


    public final int ID_MIN = 0;
    public final int PARAM_MIN = 0;
    public final int PARAM_MAX = 10000;
    public final int DESCRIPTION_MIN_LENGTH = 2;
    public final int DESCRIPTION_MAX_LENGTH = 50;

    private StringBuilder message;
    private boolean valid = true;
    private final FailFields failFields = new FailFields();

    @Override
    public Constant attribute() {
        return OtherAttribute.VALIDATOR_MEAL;
    }

    @Override
    public void catchEx(Constant field, Exception exception) {
        createMessageIfNull();
        message.append(exception.getClass().getName()).append(" for field ").append(field.value()).append(SEPARATOR);
    }

    public void id(int id) {
        if (id < ID_MIN) {
            createMessageIfNull();
            invalid();
            invalid(PARAM_ID);
            appendMustBeHigher(PARAM_ID.value(), id);
        }
    }

    public void dateTime(LocalDateTime dateTime) {
    }

    public void description(String description) {
        if (description.length() < DESCRIPTION_MIN_LENGTH) {
            createMessageIfNull();
            invalid();
            invalid(PARAM_DESCRIPTION);
            appendMustBeHigher(PARAM_DESCRIPTION.value(), description);
        } else if (description.length() > DESCRIPTION_MAX_LENGTH) {
            createMessageIfNull();
            invalid(PARAM_DESCRIPTION);
            appendMustBeLower(PARAM_DESCRIPTION.value(), description);
        }
    }

    public void params(MealAttribute[] params, int[] values) {
        if (params.length != values.length) throw new RuntimeException("params length cant be different from values length");
        for (int i = 0; i < params.length; i++) {
            if (values[i] < PARAM_MIN || values[i] > PARAM_MAX) {
                createMessageIfNull();
                invalid();
                invalid(params[i]);
                if (values[i] < PARAM_MIN) {
                    appendMustBeHigher(params[i].toString(), PARAM_MIN);
                } else if (values[i] > PARAM_MAX) {
                    appendMustBeLower(params[i].toString(), PARAM_MAX);
                }
            }
        }
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

    public boolean isValid(MealAttribute field) {
        return failFields.isValid(field);
    }

    public boolean isNoValid(MealAttribute field) {
        return failFields.isNoValid(field);
    }

    public void invalid(MealAttribute field) {
        failFields.invalid(field);
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
