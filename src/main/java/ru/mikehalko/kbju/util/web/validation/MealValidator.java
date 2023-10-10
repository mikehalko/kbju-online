package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.MealField;
import ru.mikehalko.kbju.web.constant.OtherConstant;

import static ru.mikehalko.kbju.web.constant.attribute.MealField.*;

import java.time.LocalDateTime;

public class MealValidator extends Validator<MealField> {

    private static final int ID_MIN = 0;
    private static final int PARAM_MIN = 0;
    private static final int PARAM_MAX = 10000;
    private static final int DESCRIPTION_MIN_LENGTH = 2;
    private static final int DESCRIPTION_MAX_LENGTH = 50;

    private static final boolean[] cantBeEmpty = new boolean[values().length];
    static {
        cantBeEmpty[PARAM_DESCRIPTION.ordinal()] = true;
    }

    @Override
    public Constant attribute() {
        return OtherConstant.VALIDATOR_MEAL;
    }

    @Override
    public void empty(MealField field) {
        if (cantBeEmpty[field.ordinal()]) invalid(field, String.format(MESSAGE_CANT_BE_EMPTY, field));
    }

    public void id(int id) {
        if (id < ID_MIN) {
            createMessageIfNull();
            invalid();
            invalid(PARAM_ID);
            appendMustBeHigher(PARAM_ID, id);
        }
    }

    public void dateTime(LocalDateTime dateTime) {
    }

    public void description(String description) {
        if (description.length() < DESCRIPTION_MIN_LENGTH) {
            createMessageIfNull();
            invalid();
            invalid(PARAM_DESCRIPTION);
            appendMustBeHigher(PARAM_DESCRIPTION, description);
        } else if (description.length() > DESCRIPTION_MAX_LENGTH) {
            createMessageIfNull();
            invalid(PARAM_DESCRIPTION);
            appendMustBeLower(PARAM_DESCRIPTION, description);
        }
    }

    public void params(MealField[] fields, int[] values) {
        if (fields.length != values.length) throw new RuntimeException("fields length cant be different from values length");
        for (int i = 0; i < fields.length; i++) {
            if (values[i] < PARAM_MIN || values[i] > PARAM_MAX) {
                createMessageIfNull();
                invalid();
                invalid(fields[i]);
                if (values[i] < PARAM_MIN) {
                    appendMustBeHigher(fields[i], PARAM_MIN);
                } else if (values[i] > PARAM_MAX) {
                    appendMustBeLower(fields[i], PARAM_MAX);
                }
            }
        }
    }
}
