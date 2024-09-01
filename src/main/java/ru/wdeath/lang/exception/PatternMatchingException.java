package ru.wdeath.lang.exception;

public class PatternMatchingException extends RuntimeException{

    public PatternMatchingException() {
    }

    public PatternMatchingException(String message) {
        super(message);
    }
}
