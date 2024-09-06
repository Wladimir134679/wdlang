package ru.wdeath.lang.exception;

public class WdlParserException extends RuntimeException{

    public WdlParserException() {
        super();
    }

    public WdlParserException(String message) {
        super(message);
    }
}
