package ru.mikehalko.kbju.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.OtherConstant;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialField;

import static ru.mikehalko.kbju.web.constant.attribute.UserCredentialField.*;

public class UserCredentialValidator extends Validator<UserCredentialField> {

    public final int LOGIN_LENGTH_MIN = 3;
    public final int LOGIN_LENGTH_MAX = 15;
    public final int PASSWORD_LENGTH_MIN = 3;
    public final int PASSWORD_LENGTH_MAX = 12;

    @Override
    public Constant attribute() {
        return OtherConstant.VALIDATOR_USER_CREDENTIAL;
    }

    @Override
    public void empty(UserCredentialField field) {
        super.empty(field);
    }

    public void login(String name) {
        if (name.length() < LOGIN_LENGTH_MIN) {
            invalid(PARAM_LOGIN);
            appendMustBeHigher(PARAM_LOGIN, LOGIN_LENGTH_MIN);
        } else if (name.length() > LOGIN_LENGTH_MAX) {
            invalid(PARAM_LOGIN);
            appendMustBeLower(PARAM_LOGIN, LOGIN_LENGTH_MAX);
        }
    }

    public void password(String pass) {
        if (pass.length() < PASSWORD_LENGTH_MIN) {
            invalid(PARAM_PASSWORD_NEW);
            appendMustBeHigher(PARAM_PASSWORD_NEW, PASSWORD_LENGTH_MIN);
        } else if (pass.length() > PASSWORD_LENGTH_MAX) {
            invalid(PARAM_PASSWORD_NEW);
            appendMustBeLower(PARAM_PASSWORD_NEW, PASSWORD_LENGTH_MAX);
        }
    }

    public void passwordRepeatEquals(String pass, String repeat) {
        if (! pass.equals(repeat)) {
            invalid(PARAM_PASSWORD_REPEAT);
            appendWithSeparator("passwords mismatch");
        }
    }
}
