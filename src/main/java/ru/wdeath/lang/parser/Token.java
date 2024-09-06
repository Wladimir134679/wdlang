package ru.wdeath.lang.parser;

public record Token(TokenType type, String text, Pos pos) {

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", pos=" + pos +
                '}';
    }
}
