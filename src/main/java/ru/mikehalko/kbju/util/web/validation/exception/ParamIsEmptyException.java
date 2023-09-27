package ru.mikehalko.kbju.util.web.validation.exception;

public class ParamIsEmptyException extends RuntimeException {
    public ParamIsEmptyException() {
    }

    public ParamIsEmptyException(String message) {
        super(message);
    }
}
