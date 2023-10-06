package ru.mikehalko.kbju.web.exception;

public class HttpSessionNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Request has no valid HTTP session";
    public HttpSessionNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public HttpSessionNotFoundException(String message) {
        super(message);
    }
}
