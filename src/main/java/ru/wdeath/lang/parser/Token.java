package ru.wdeath.lang.parser;

import ru.wdeath.lang.utils.Pos;

public record Token(TokenType type, String text, Pos pos) {

    public String shortDescription() {
        return type().name() + " " + text;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", pos=" + pos +
                '}';
    }
}
