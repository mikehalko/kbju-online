package ru.mikehalko.kbju.util.sql.exception;


public class PropertiesFileNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Wrong path db.properties or file not exist";
    public PropertiesFileNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}
