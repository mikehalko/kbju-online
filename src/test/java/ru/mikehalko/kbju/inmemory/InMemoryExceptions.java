package ru.mikehalko.kbju.inmemory;

public class InMemoryExceptions {
    public static class NotFoundException extends RuntimeException {
        public NotFoundException() {
        }
        public NotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotOwnException extends RuntimeException {
        public UserNotOwnException() {
        }
        public UserNotOwnException(String s) {
        }
    }
}
