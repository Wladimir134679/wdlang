package ru.wdeath.lang.exception;

public class ArgumentsMismatchException extends WdlRuntimeException{

    public ArgumentsMismatchException() {
    }

    public ArgumentsMismatchException(String message) {
        super(message);
    }
}
