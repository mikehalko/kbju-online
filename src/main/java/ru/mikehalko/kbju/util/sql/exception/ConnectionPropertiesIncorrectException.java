package ru.mikehalko.kbju.util.sql.exception;


public class ConnectionPropertiesIncorrectException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Wrong user, pass or url-DB";

    public ConnectionPropertiesIncorrectException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
