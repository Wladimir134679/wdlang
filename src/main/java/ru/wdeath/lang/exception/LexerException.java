package ru.wdeath.lang.exception;

import ru.wdeath.lang.parser.Pos;

public class LexerException extends WdlParserException {

    public LexerException(String message) {
        super(message);
    }

    public LexerException(String message, Pos pos) {
        super(pos.format() + " " + message);
    }
}
