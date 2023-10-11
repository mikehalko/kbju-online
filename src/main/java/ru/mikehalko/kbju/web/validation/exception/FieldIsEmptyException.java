package ru.mikehalko.kbju.web.validation.exception;

public class FieldIsEmptyException extends Exception {
    public FieldIsEmptyException() {
    }

    public FieldIsEmptyException(String message) {
        super(message);
    }
}
