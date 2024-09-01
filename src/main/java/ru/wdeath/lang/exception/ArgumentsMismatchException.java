package ru.wdeath.lang.exception;

public class ArgumentsMismatchException extends RuntimeException{

    public ArgumentsMismatchException() {
    }

    public ArgumentsMismatchException(String message) {
        super(message);
    }
}
