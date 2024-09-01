package ru.wdeath.lang.exception;

public class ParseException extends RuntimeException{

    public ParseException() {
        super();
    }

    public ParseException(String string) {
        super(string);
    }
}
