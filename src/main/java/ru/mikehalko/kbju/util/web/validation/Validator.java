package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.FieldAttribute;


public abstract class Validator <T extends FieldAttribute> implements Validation {
    private static final String SEPARATOR = ";\n";
    public static final String MESSAGE_MUST_BE_HIGHER = "%s must be higher %s";
    public static final String MESSAGE_MUST_BE_LOWER = "%s must be lower %s";

    private StringBuilder message;
    private boolean valid = true;
    private final FailFields failFields = new FailFields();

    public abstract Constant attribute();

    public void catchEx(Constant param, Exception exception) {
        invalid();
        message.append(exception.getClass().getName()).append(" for field ").append(param.value()).append(SEPARATOR);
    }

    public void invalid(T field) {
        invalid();
        failFields.invalid(field);
    }

    public void invalid(T field, String message) {
        invalid(field);
        appendWithSeparator(message);
    }

    public void invalid() {
        createMessageIfNull();
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public boolean isValid(T field) {
        return failFields.isValid(field);
    }

    public boolean isNoValid(T field) {
        return failFields.isNoValid(field);
    }


    public String resultMessage() {
        return message != null ? message.toString() : "";
    }

    protected void createMessageIfNull() {
        message = message == null ? new StringBuilder() : message;
    }


    protected void appendMustBeHigher(String param, Object value) {
        appendWithSeparator(String.format(MESSAGE_MUST_BE_HIGHER, param, value));
    }

    protected void appendMustBeLower(String param, Object value) {
        appendWithSeparator(String.format(MESSAGE_MUST_BE_LOWER, param, value));
    }

    protected void appendWithSeparator(String text) {
        createMessageIfNull();
        message.append(text).append(SEPARATOR);
    }
}
