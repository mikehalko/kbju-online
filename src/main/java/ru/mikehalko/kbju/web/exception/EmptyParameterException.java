package ru.mikehalko.kbju.web.exception;

import ru.mikehalko.kbju.web.constant.Constant;

public class EmptyParameterException extends BadParameterException {
    private static final String DEFAULT_MESSAGE = "Parameter cannot be empty for parsing, parameter id = ";

    public EmptyParameterException(Constant param) {
        super(DEFAULT_MESSAGE + param.value(), param);
    }
}
