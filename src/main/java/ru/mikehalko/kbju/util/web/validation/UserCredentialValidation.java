package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.OtherAttribute;
import ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute;

import static ru.mikehalko.kbju.web.constant.attribute.UserCredentialAttribute.*;


// TODO извлечь общий класс из всех Validation реализаций
public class UserCredentialValidation implements Validation {
    private static final String SEPARATOR = ";\n";
    public static final String MESSAGE_MUST_BE_HIGHER = "%s must be higher %s"; // TODO перевод
    public static final String MESSAGE_MUST_BE_LOWER = "%s must be lower %s";

    public final boolean LOGIN_EMPTY = false;
    public final boolean PASSWORD_EMPTY = false;
    public final boolean PASSWORD_REPEAT_EMPTY = false;

    public final int LOGIN_LENGTH_MIN = 3;
    public final int LOGIN_LENGTH_MAX = 15;
    public final int PASSWORD_LENGTH_MIN = 3;
    public final int PASSWORD_LENGTH_MAX = 12;

    private StringBuilder message;
    private boolean valid = true;
    private final FailFields failFields = new FailFields();

    @Override
    public Constant attribute() {
        return OtherAttribute.VALIDATOR_USER_CREDENTIAL;
    }

    @Override
    public void catchEx(Constant param, Exception exception) {
        createMessageIfNull();
        message.append(exception.getClass().getName()).append(" for field ").append(param.value()).append(SEPARATOR);
    }

    public void login(String name) {
        if (name.length() < LOGIN_LENGTH_MIN) {
            invalid(PARAM_LOGIN);
            appendMustBeHigher(PARAM_LOGIN.value(), LOGIN_LENGTH_MIN);
        } else if (name.length() > LOGIN_LENGTH_MAX) {
            invalid(PARAM_LOGIN);
            appendMustBeLower(PARAM_LOGIN.value(), LOGIN_LENGTH_MAX);
        }
    }

    public void password(String pass) {
        if (pass.length() < PASSWORD_LENGTH_MIN) {
            invalid(PARAM_PASSWORD_NEW);
            appendMustBeHigher(PARAM_PASSWORD_NEW.value(), PASSWORD_LENGTH_MIN);
        } else if (pass.length() > PASSWORD_LENGTH_MAX) {
            invalid(PARAM_PASSWORD_NEW);
            appendMustBeLower(PARAM_PASSWORD_NEW.value(), PASSWORD_LENGTH_MAX);
        }
    }

    public void passwordRepeatEquals(String pass, String repeat) {
        if (! pass.equals(repeat)) {
            invalid(PARAM_PASSWORD_REPEAT);
            appendWithSeparator("passwords mismatch");
        }
    }

    public void invalid(UserCredentialAttribute field) {
        createMessageIfNull();
        invalid();
        failFields.invalid(field);
    }

    public void invalid(UserCredentialAttribute field, String message) {
        invalid(field);
        appendWithSeparator(message);
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

    public boolean isValid(UserCredentialAttribute field) {
        return failFields.isValid(field);
    }

    public boolean isNoValid(UserCredentialAttribute field) {
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

    public void appendWithSeparator(String text) {
        createMessageIfNull();
        message.append(text).append(SEPARATOR);
    }
}
