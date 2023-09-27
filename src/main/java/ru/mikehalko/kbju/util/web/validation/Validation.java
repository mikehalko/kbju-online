package ru.mikehalko.kbju.util.web.validation;

public interface Validation {
    void catchEx(String field, Exception exception);

    boolean isNotValid();
}
