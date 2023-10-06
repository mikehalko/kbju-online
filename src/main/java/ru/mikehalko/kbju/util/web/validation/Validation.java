package ru.mikehalko.kbju.util.web.validation;

import ru.mikehalko.kbju.web.constant.Constant;

public interface Validation {
    void catchEx(Constant field, Exception exception);

    boolean isNotValid();

    Constant attribute();
}
