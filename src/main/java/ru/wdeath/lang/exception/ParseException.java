package ru.wdeath.lang.exception;

import ru.wdeath.lang.utils.Range;

public class ParseException extends RuntimeException {

    private final Range range;

    public ParseException(String message, Range range) {
        super(message);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }
}
