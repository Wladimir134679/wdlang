package ru.wdeath.lang.exception;

import ru.wdeath.lang.parser.Pos;
import ru.wdeath.lang.parser.Range;

public class ParseException extends WdlParserException {

    private final Range range;

    public ParseException(String message) {
        this(message, Range.ZERO);
    }

    public ParseException(String message, Pos pos) {
        this(message, pos, pos);
    }

    public ParseException(String message, Pos start, Pos end) {
        this(message, new Range(start, end));
    }

    public ParseException(String message, Range range) {
        super(message);
        this.range = range;
    }

    public Range getRange() {
        return range;
    }
}
