package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;
import ru.mikehalko.kbju.web.constant.attribute.FieldAttribute;



public abstract class Validator <T extends FieldAttribute> implements Validation {
    private static final String SEPARATOR = ";\n";
    public static final String MESSAGE_MUST_BE_HIGHER = "%s must be higher %s";
    public static final String MESSAGE_MUST_BE_LOWER = "%s must be lower %s";
    public static final String MESSAGE_CANT_BE_EMPTY = "%s cant be empty";

    private StringBuilder message;
    private boolean valid = true;
    private final FailFields failFields = new FailFields();

    @Override
    public abstract Constant attribute();

    @Override
    public void catchEx(Constant param, Exception exception) {
        invalid();
        message.append(exception.getClass().getName()).append(" for field ").append(param.value()).append(SEPARATOR);
    }

    @Override
    public void invalid() {
        createMessageIfNull();
        this.valid = false;
    }

    public void invalid(T field) {
        invalid();
        failFields.invalid(field);
    }

    public void invalid(T field, String message) {
        invalid(field);
        appendWithSeparator(message);
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isNotValid() {
        return !isValid();
    }

    public boolean isValid(T field) {
        return failFields.isValid(field);
    }

    public boolean isNoValid(T field) {
        return failFields.isNoValid(field);
    }

    public void empty(T field) {
        invalid(field, String.format(MESSAGE_CANT_BE_EMPTY, field));
    }


    public String resultMessage() {
        return message != null ? message.toString() : "";
    }

    protected void createMessageIfNull() {
        message = message == null ? new StringBuilder() : message;
    }


    protected void appendMustBeHigher(T field, Object value) {
        appendWithSeparator(String.format(MESSAGE_MUST_BE_HIGHER, field, value));
    }

    protected void appendMustBeLower(T field, Object value) {
        appendWithSeparator(String.format(MESSAGE_MUST_BE_LOWER, field, value));
    }

    protected void appendWithSeparator(String text) {
        createMessageIfNull();
        message.append(text).append(SEPARATOR);
    }
}
