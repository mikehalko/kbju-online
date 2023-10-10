package ru.mikehalko.kbju.util.web.exception;

import ru.mikehalko.kbju.web.constant.Constant;

public class NotExistParameterException extends BadParameterException {
    private static final String DEFAULT_MESSAGE = "Parameter does not exist in request scope, parameter id = ";

    public NotExistParameterException(Constant param) {
        super(DEFAULT_MESSAGE + param.value(), param);
    }
}
