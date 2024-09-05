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
    MATCH, CASE,
    CLASS,
    NEW,

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

    PLUSEQ, // +=
    MINUSEQ, // -=
    STAREQ, // *=
    SLASHEQ, // /=
    PERCENTEQ, // %=
    AMPEQ, // &=
    CARETEQ, // ^=
    BAREQ, // |=
    COLONCOLONEQ, // ::=
    LTLTEQ, // <<=
    GTGTEQ, // >>=
    GTGTGTEQ, // >>>=

    PLUSPLUS, // ++
    MINUSMINUS, // --

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
    DOT, // .

    EOF,
}
