package ru.wdeath.lang.parser;

public enum TokenType {

    NUMBER,
    HEX_NUMBER,
    WORD,
    TEXT,

    // KEYWORD
    PRINT,
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    BREAK,
    CONTINUE,
    DEF,
    RETURN,

    STAR,
    PLUS,
    MINUS,
    SLASH,
    EQ,
    EQEQ,
    EXCL,
    EXCLEQ,
    LT,
    LTEQ,
    GT,
    GTEQ,

    BAR,
    BARBAR,
    AMP,
    AMPAMP,

    LPAREN,// (
    RPAREN,// )
    LBRACKET,// [
    RBRACKET,// ]
    LBRACE, // {
    RBRACE, // }

    COMMA, // ,

    EOF,
}
