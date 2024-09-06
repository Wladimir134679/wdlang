package ru.wdeath.lang.exception;

import ru.wdeath.lang.parser.error.ParseError;
import ru.wdeath.lang.parser.error.ParseErrors;

public class WdlParserException extends RuntimeException{

    private final ParseErrors parseErrors;

    public WdlParserException(ParseError parseError) {
        super(parseError.toString());
        this.parseErrors = new ParseErrors();
        parseErrors.add(parseError);;
    }

    public WdlParserException(ParseErrors parseErrors) {
        super(parseErrors.toString());
        this.parseErrors = parseErrors;
    }

    public ParseErrors getParseErrors() {
        return parseErrors;
    }
}
