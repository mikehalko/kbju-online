package ru.mikehalko.kbju.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;

public interface Validation {
    void catchEx(Constant field, Exception exception);

    void invalid();

    boolean isValid();

    boolean isNotValid();

    String resultMessage();

    Constant attribute();
}
