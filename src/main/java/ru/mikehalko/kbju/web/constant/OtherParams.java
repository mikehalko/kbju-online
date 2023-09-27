package ru.mikehalko.kbju.web.constant;

public enum OtherParams implements Params {

    PARAM_ACTION("action");

    private final String paramValue;

    private OtherParams(String paramValue) {
        this.paramValue = paramValue;
    }

    public String value() {
        return paramValue;
    }

    @Override
    public String toString() {
        return paramValue;
    }
}
