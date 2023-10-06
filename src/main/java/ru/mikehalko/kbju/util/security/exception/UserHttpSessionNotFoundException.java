package ru.mikehalko.kbju.util.security.exception;

public class UserHttpSessionNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User in session not found";
    public UserHttpSessionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
