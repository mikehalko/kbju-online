package ru.mikehalko.kbju.web.constant.attribute;

public enum UserCredentialField implements FieldAttribute {

    PARAM_LOGIN("login"),
    PARAM_PASSWORD("password"),
    PARAM_PASSWORD_OLD("password_old"),
    PARAM_PASSWORD_NEW("password_new"),
    PARAM_PASSWORD_REPEAT("password_repeat");

    private final String paramValue;

    private UserCredentialField(String paramValue) {
        this.paramValue = paramValue;
    }

    public String value() {
        return paramValue;
    }

    @Override
    public Field fieldType() {
        return Field.USER_CREDENTIAL;
    }

    @Override
    public String toString() {
        return paramValue;
    }
}
