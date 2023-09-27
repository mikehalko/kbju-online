package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.MealParams;
import static ru.mikehalko.kbju.web.constant.MealParams.*;

import java.time.LocalDateTime;

public class MealValidation implements Validation {
    private static final String SEPARATOR = ";\n";
    public static final String MESSAGE_MUST_BE_HIGHER = "%s must be higher %s"; // TODO перевод
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
    public void catchEx(String field, Exception exception) {
        createMessageIfNull();
        message.append(exception.getClass().getName()).append(" for field ").append(field).append(SEPARATOR);
    }

    public void id(MealParams param, int id) { // TODO УБРАТЬ ПАРАМЕТР!
        if (id < ID_MIN) {
            createMessageIfNull();
            invalid();
            invalid(PARAM_ID);
            appendMustBeHigher(param.value(), id);
        }
    }

    public void dateTime(MealParams param, LocalDateTime dateTime) {
        // ...
    }

    public void description(MealParams param, String description) { // TODO УБРАТЬ ПАРАМЕТР!
        if (description.length() < DESCRIPTION_MIN_LENGTH) {
            createMessageIfNull();
            invalid();
            invalid(PARAM_DESCRIPTION);
            appendMustBeHigher(param.value(), description);
        } else if (description.length() > DESCRIPTION_MAX_LENGTH) {
            createMessageIfNull();
            invalid(PARAM_DESCRIPTION);
            appendMustBeLower(param.value(), description);
        }
    }

    public void params(MealParams[] params, int[] values) {
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

    public boolean isValid(MealParams field) {
        return failFields.isValid(field);
    }

    public boolean isNoValid(MealParams field) {
        return failFields.isNoValid(field);
    }

    public void invalid(MealParams field) {
        failFields.invalid(field);
    }


    public String resultMessage() {
        return message.toString();
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

    public static void main(String[] args) {

    }
}
