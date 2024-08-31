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

    PLUS, // +
    MINUS, // -
    STAR, // *
    SLASH, // /
    PERCENT,// %

    EQ,
    EQEQ,
    EXCL,
    EXCLEQ,
    LT,
    LTEQ,
    GT,
    GTEQ,

    BAR, // |
    BARBAR, // ||
    AMP, // &
    AMPAMP, // &&

    TILDE, // ~
    CARET, // ^
    CARETCARET, // ^^
    LTLT, // <<
    GTGT, // >>
    GTGTGT, // >>>

    LPAREN,// (
    RPAREN,// )
    LBRACKET,// [
    RBRACKET,// ]
    LBRACE, // {
    RBRACE, // }

    QUESTION, // ?
    COLON, // :
    COLONCOLON, // ::
    COMMA, // ,

    EOF,
}
