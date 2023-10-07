package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.OtherConstant;
import ru.mikehalko.kbju.web.constant.attribute.UserField;

import static ru.mikehalko.kbju.web.constant.attribute.UserField.*;

public class UserValidator extends Validator<UserField> {

    public final int USER_ID_MIN = 0;
    public final int NAME_LENGTH_MIN = 3;
    public final int NAME_LENGTH_MAX = 15;
    public final int CALORIES_MIN_MIN = 0;
    public final int CALORIES_MIN_MAX = 10000;
    public final int CALORIES_MAX_MIN = 0;
    public final int CALORIES_MAX_MAX = 10000;
    public final boolean CALORIES_MIN_HIGHER_MAX = false;

    @Override
    public Constant attribute() {
        return OtherConstant.VALIDATOR_USER;
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
            appendMustBeHigher(PARAM_NAME.value(), NAME_LENGTH_MIN);
        } else if (name.length() > NAME_LENGTH_MAX) {
            invalid(PARAM_NAME);
            appendMustBeLower(PARAM_NAME.value(), NAME_LENGTH_MAX);
        }
    }

    public void calories(int min, int max) {
        if (min < CALORIES_MIN_MIN) {
            invalid(PARAM_CALORIES_MIN);
            appendMustBeHigher(PARAM_CALORIES_MIN.value(), CALORIES_MIN_MIN);
        } else if (min > CALORIES_MIN_MAX) {
            invalid(PARAM_CALORIES_MIN);
            appendMustBeLower(PARAM_CALORIES_MIN.value(), CALORIES_MIN_MAX);
        }

        if (max < CALORIES_MAX_MIN) {
            invalid(PARAM_CALORIES_MAX);
            appendMustBeHigher(PARAM_CALORIES_MAX.value(), CALORIES_MAX_MIN);
        } else if (max > CALORIES_MAX_MAX) {
            invalid(PARAM_CALORIES_MAX);
            appendMustBeLower(PARAM_CALORIES_MAX.value(), CALORIES_MAX_MAX);
        }

        if (!CALORIES_MIN_HIGHER_MAX && min > max) {
            invalid(PARAM_CALORIES_MIN);
            invalid(PARAM_CALORIES_MAX);
            appendWithSeparator(String.format("%s cant be higher than %s", PARAM_CALORIES_MIN, PARAM_CALORIES_MAX));
        }
    }
}
