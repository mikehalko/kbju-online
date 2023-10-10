package ru.mikehalko.kbju.util.web.exception;

import ru.mikehalko.kbju.web.constant.Constant;

public abstract class BadParameterException extends Exception {
    private final Constant causeParam;

    public BadParameterException(String message, Constant param) {
        super(message);
        this.causeParam = param;
    }

    public BadParameterException(String message) {
        super(message);
        this.causeParam = null;
    }

    public Constant cause() {
        return causeParam;
    }
}
