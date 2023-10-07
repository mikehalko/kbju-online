package ru.mikehalko.kbju.util.web.validation.exception;


import ru.mikehalko.kbju.web.constant.attribute.Field;

public class WrongTypeEnumFieldException extends RuntimeException {
    private static final String REQUIRED_TYPE = Field.class.getName();
    private static final String DEFAULT_MESSAGE = String.format("Required type: %s, but provided: ", REQUIRED_TYPE);
    public WrongTypeEnumFieldException(Class<?> provided) {
        super(DEFAULT_MESSAGE + provided.getName());
    }
}
