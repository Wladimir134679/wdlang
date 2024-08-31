package ru.wdeath.lang.parser;

public class ParseException extends RuntimeException{

    public ParseException() {
        super();
    }

    public ParseException(String string) {
        super(string);
    }
}
