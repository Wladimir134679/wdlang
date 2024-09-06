package ru.wdeath.lang.exception;

import ru.wdeath.lang.parser.Range;

public class BaseParserException extends RuntimeException{

    private final Range range;

    public BaseParserException(String message, Range range) {
        super(message);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }
}
