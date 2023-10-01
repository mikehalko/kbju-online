package ru.mikehalko.kbju.web.constant.attribute;

import ru.mikehalko.kbju.web.constant.Constant;

public enum UserCredentialAttribute implements Constant {

    PARAM_LOGIN("login"),
    PARAM_PASSWORD("password"),
    PARAM_PASSWORD_OLD("password_old"),
    PARAM_PASSWORD_NEW("password_new"),
    PARAM_PASSWORD_REPEAT("password_repeat");

    private final String paramValue;

    private UserCredentialAttribute(String paramValue) {
        this.paramValue = paramValue;
    }

    public String value() {
        return paramValue;
    }

    @Override
    public String toString() {
        return paramValue;
    }
}
