package ru.mikehalko.kbju.util.sql.exception;


public class DriverClassNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Driver class db not found";

    public DriverClassNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
