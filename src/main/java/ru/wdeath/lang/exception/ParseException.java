package ru.wdeath.lang.exception;

import ru.wdeath.lang.utils.Range;

public class ParseException extends BaseParserException {

    public ParseException(String message) {
        super(message, Range.ZERO);
    }

    public ParseException(String message, Range range) {
        super(message, range);
    }
}
