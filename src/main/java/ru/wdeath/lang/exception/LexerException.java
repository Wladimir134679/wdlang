package ru.wdeath.lang.exception;

import ru.wdeath.lang.parser.Pos;

public class LexerException extends RuntimeException {

    public LexerException(String message) {
        super(message);
    }

    public LexerException(Pos pos, String message) {
        super(pos + " " + message);
    }
}
