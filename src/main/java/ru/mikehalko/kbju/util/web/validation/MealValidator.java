package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.MealField;
import ru.mikehalko.kbju.web.constant.OtherConstant;

import static ru.mikehalko.kbju.web.constant.attribute.MealField.*;

import java.time.LocalDateTime;

public class MealValidator extends Validator<MealField> {

    public final int ID_MIN = 0;
    public final int PARAM_MIN = 0;
    public final int PARAM_MAX = 10000;
    public final int DESCRIPTION_MIN_LENGTH = 2;
    public final int DESCRIPTION_MAX_LENGTH = 50;

    @Override
    public Constant attribute() {
        return OtherConstant.VALIDATOR_MEAL;
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

    public void params(MealField[] params, int[] values) {
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
}
