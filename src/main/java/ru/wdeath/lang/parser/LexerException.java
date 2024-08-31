package ru.wdeath.lang.parser;

public class LexerException extends RuntimeException{

    public LexerException(String message) {
        super(message);
    }

    public LexerException(int row, int col, String message) {
        super("["+row+":"+col+"] " + message);
    }
}
