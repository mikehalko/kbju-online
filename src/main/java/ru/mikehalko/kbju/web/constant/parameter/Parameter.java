package ru.mikehalko.kbju.web.constant.parameter;

import ru.mikehalko.kbju.web.constant.Constant;

import java.util.HashMap;
import java.util.Map;

public enum Parameter implements Constant {
    SERVLET_USER("user"),
    SERVLET_MEAL("meals"),
    SERVLET_LOGIN("login"),

    ACTION("action"),
    ACTION_GET_ALL("get_all"),
    ACTION_GET("get"),
    ACTION_UPDATE("update"),
    ACTION_CREATE("create"),
    ACTION_DELETE("delete"),
    ACTION_LOGIN("login"),
    ACTION_REGISTER("register"),
    ACTION_LOGOUT("out");

    private static final Map<String, Integer> valuesKeysMap;

    static {
        valuesKeysMap = new HashMap<>();
        for (Parameter param : Parameter.values()) {
            valuesKeysMap.put(param.value(), param.ordinal());
        }
    }


    private final String paramValue;

    private Parameter(String paramValue) {
        this.paramValue = paramValue;
    }

    public String value() {
        return paramValue;
    }

    public static Parameter byValue(String name) {
        try {
            return Parameter.values()[valuesKeysMap.get(name)];
        } catch (NullPointerException ignored) {
            throw new IllegalArgumentException(Parameter.class.getName() +
                    " enum type has no constant with the specified name = " +
                    name);
        }
    }

    @Override
    public String toString() {
        return paramValue;
    }
}
