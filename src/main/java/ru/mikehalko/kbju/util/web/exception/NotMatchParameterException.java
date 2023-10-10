package ru.mikehalko.kbju.util.web.exception;

public class NotMatchParameterException extends BadParameterException {
    private static final String DEFAULT_MESSAGE = "Parameter cannot be empty for parsing, parameter id = ";

    public NotMatchParameterException(String provided) {
        super(DEFAULT_MESSAGE + provided);
    }
}
