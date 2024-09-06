package ru.wdeath.lang.exception;

public class WdlRuntimeException extends RuntimeException{

    public WdlRuntimeException() {
        super();
    }

    public WdlRuntimeException(String message) {
        super(message);
    }

    public WdlRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
