package ru.wdeath.lang.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private static final String OPERATION_CHARS = "+-*/()=<>!&|{},[]%?:~^.";

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
        OPERATORS.put("[", TokenType.LBRACKET);
        OPERATORS.put("]", TokenType.RBRACKET);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);

        OPERATORS.put("%", TokenType.PERCENT);
        OPERATORS.put(",", TokenType.COMMA);
        OPERATORS.put(".", TokenType.DOT);
        OPERATORS.put("?", TokenType.QUESTION);
        OPERATORS.put(":", TokenType.COLON);

        OPERATORS.put("!", TokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);

        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put("!=", TokenType.EXCLEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put(">=", TokenType.GTEQ);

        OPERATORS.put("::", TokenType.COLONCOLON);

        OPERATORS.put("~", TokenType.TILDE);
        OPERATORS.put("^", TokenType.CARET);
        OPERATORS.put("^^", TokenType.CARETCARET);
        OPERATORS.put(">>", TokenType.GTGT);
        OPERATORS.put("<<", TokenType.LTLT);
        OPERATORS.put(">>>", TokenType.GTGTGT);

        OPERATORS.put("&&", TokenType.AMPAMP);
        OPERATORS.put("||", TokenType.BARBAR);

    }
    private static final Map<String, TokenType> KEYWORDS;
    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("print", TokenType.PRINT);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("break", TokenType.BREAK);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        KEYWORDS.put("def", TokenType.DEF);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("match", TokenType.MATCH);
        KEYWORDS.put("case", TokenType.CASE);
    }

    private final String input;
    private final int length;
    private final List<Token> tokens;
    private final StringBuilder buffer;
    private int pos;
    private int row, col;

    public Lexer(String input) {
        this.input = input;
        this.length = input.length();
        this.pos = 0;
        this.tokens = new ArrayList<>();
        this.buffer = new StringBuilder();
        row = col = 1;
    }

    public List<Token> tokenize() {
        while (pos < length) {
            final var current = peek(0);
            if (Character.isDigit(current)) tokenizeNumber();
            else if (Character.isJavaIdentifierStart(current)) tokenizeWorld();
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
        clearBuffer();
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
        clearBuffer();
        char current = peek(0);
        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw error("Invalid float number");;
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
        clearBuffer();
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
        clearBuffer();
        char current = peek(0);
        while (true) {
            if (!Character.isLetterOrDigit(current) && (current != '_') && (current != '$'))
                break;
            buffer.append(current);
            current = next();
        }
        String word = buffer.toString();
        if (KEYWORDS.containsKey(word)) {
            addToken(KEYWORDS.get(word));
        } else {
            addToken(TokenType.WORD, word);
        }
    }

    private void tokenizeText() {
        next();// Skip "
        clearBuffer();
        char current = peek(0);
        while (true) {
            if (current == '\0') throw error("Reached end of file while parsing text string.");
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
                    case 'b':
                        current = next();
                        buffer.append('\b');
                        continue;
                    case 'f':
                        current = next();
                        buffer.append('\f');
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
            if (current == '\0') throw error("Missing close tag");
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
        final char result = peek(0);
        if (result == '\n') {
            row++;
            col = 1;
        } else col++;
        return result;
    }

    private char peek(int relativePosition) {
        final var position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private LexerException error(String text) {
        return new LexerException(row, col, text);
    }

    private void clearBuffer() {
        buffer.setLength(0);
    }

    private void addToken(TokenType type) {
        this.addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text, row, col));
    }
}
