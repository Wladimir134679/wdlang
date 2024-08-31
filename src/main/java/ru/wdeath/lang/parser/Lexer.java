package ru.wdeath.lang.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private static final String OPERATION_CHARS = "+-*/()=<>!&|{},";

    private static final Map<String, TokenType> OPERATORS = new HashMap<>();

    static {
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("(", TokenType.LPAREN);
        OPERATORS.put(")", TokenType.RPAREN);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);

        OPERATORS.put(",", TokenType.COMMA);

        OPERATORS.put("!", TokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);

        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put("!=", TokenType.EXCLEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put(">=", TokenType.GTEQ);

        OPERATORS.put("&&", TokenType.AMPAMP);
        OPERATORS.put("||", TokenType.BARBAR);

    }

    private final String input;
    private final int length;
    private List<Token> tokens;
    private int pos;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
        this.pos = 0;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        while (pos < length) {
            final var current = peek(0);
            if (Character.isDigit(current)) tokenizeNumber();
            else if (Character.isLetter(current)) tokenizeWorld();
            else if (current == '#') {
                next();
                tokenizeHexNumber();
            } else if (current == '"') {
                tokenizeText();
            } else if (OPERATION_CHARS.indexOf(current) != -1) {
                tokenizeOperation();
            } else {
                next();
            }
        }
        return tokens;
    }

    private void tokenizeHexNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (Character.isDigit(current) || isHexNumber(current)) {
            buffer.append(current);
            current = next();
        }
        addToken(TokenType.HEX_NUMBER, buffer.toString());
    }

    private static boolean isHexNumber(char current) {
        return "abcdef".indexOf(Character.toLowerCase(current)) != -1;
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException("Invalid number format");
            } else if (!Character.isDigit(current))
                break;
            buffer.append(current);
            current = next();
        }
        addToken(TokenType.NUMBER, buffer.toString());
    }

    private void tokenizeOperation() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                next();
                next();
                tokenizeMultilineComment();
                return;
            }
        }
        final StringBuilder buffer = new StringBuilder();
        while(true){
            final var text = buffer.toString();
            if(!OPERATORS.containsKey(text + current) && !text.isEmpty()){
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeWorld() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (!Character.isLetterOrDigit(current) && (current != '_') && (current != '$'))
                break;
            buffer.append(current);
            current = next();
        }
        String toString = buffer.toString();
        switch (toString) {
            case "print" -> addToken(TokenType.PRINT);
            case "if" -> addToken(TokenType.IF);
            case "else" -> addToken(TokenType.ELSE);
            case "for" -> addToken(TokenType.FOR);
            case "while" -> addToken(TokenType.WHILE);
            case "do" -> addToken(TokenType.DO);
            case "break" -> addToken(TokenType.BREAK);
            case "continue" -> addToken(TokenType.CONTINUE);
            case "def" -> addToken(TokenType.DEF);
            case "return" -> addToken(TokenType.RETURN);
            default -> addToken(TokenType.WORD, toString);
        }
    }

    private void tokenizeText() {
        next();// Skip "
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"':
                        current = next();
                        buffer.append('"');
                        continue;
                    case 'n':
                        current = next();
                        buffer.append('\n');
                        continue;
                    case 't':
                        current = next();
                        buffer.append('\t');
                        continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == '"') break;
            buffer.append(current);
            current = next();
        }
        next(); // Skip "
        addToken(TokenType.TEXT, buffer.toString());
    }

    private void tokenizeMultilineComment() {
        char current = peek(0);
        while (true) {
            if (current == '\0') throw new RuntimeException("Missing close tag");
            if (current == '*' && peek(1) == '/') break;
            current = next();
        }
        next(); // *
        next(); // /
    }

    private void tokenizeComment() {
        char current = peek(0);
        while ("\n\r\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private char next() {
        pos++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final var position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private void addToken(TokenType type) {
        this.addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text));
    }
}
