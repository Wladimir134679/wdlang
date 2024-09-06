package ru.wdeath.lang.exception;

public class PatternMatchingException extends WdlRuntimeException{

    public PatternMatchingException() {
    }

    public PatternMatchingException(String message) {
        super(message);
    }
}
