package ru.wdeath.lang.parser;

import ru.wdeath.lang.exception.LexerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private static final String OPERATION_CHARS = "+-*/()=<>!&|{},[]%?:~^.";

    private static final Map<String, TokenType> OPERATORS;

    static {
        final Map<String, TokenType> operators = new HashMap<>();
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINUS);
        operators.put("*", TokenType.STAR);
        operators.put("/", TokenType.SLASH);
        operators.put("(", TokenType.LPAREN);
        operators.put(")", TokenType.RPAREN);
        operators.put("{", TokenType.LBRACE);
        operators.put("}", TokenType.RBRACE);
        operators.put("[", TokenType.LBRACKET);
        operators.put("]", TokenType.RBRACKET);
        operators.put("=", TokenType.EQ);
        operators.put("<", TokenType.LT);
        operators.put(">", TokenType.GT);

        operators.put("%", TokenType.PERCENT);
        operators.put(",", TokenType.COMMA);
        operators.put(".", TokenType.DOT);
        operators.put("?", TokenType.QUESTION);
        operators.put(":", TokenType.COLON);

        operators.put("!", TokenType.EXCL);
        operators.put("&", TokenType.AMP);
        operators.put("|", TokenType.BAR);

        operators.put("==", TokenType.EQEQ);
        operators.put("!=", TokenType.EXCLEQ);
        operators.put("<=", TokenType.LTEQ);
        operators.put(">=", TokenType.GTEQ);

        operators.put("::", TokenType.COLONCOLON);

        operators.put("+=", TokenType.PLUSEQ);
        operators.put("-=", TokenType.MINUSEQ);
        operators.put("*=", TokenType.STAREQ);
        operators.put("/=", TokenType.SLASHEQ);
        operators.put("%=", TokenType.PERCENTEQ);
        operators.put("&=", TokenType.AMPEQ);
        operators.put("^=", TokenType.CARETEQ);
        operators.put("|=", TokenType.BAREQ);
        operators.put("::=", TokenType.COLONCOLONEQ);
        operators.put("<<=", TokenType.LTLTEQ);
        operators.put(">>=", TokenType.GTGTEQ);
        operators.put(">>>=", TokenType.GTGTGTEQ);

        operators.put("++", TokenType.PLUSPLUS);
        operators.put("--", TokenType.MINUSMINUS);


        operators.put("~", TokenType.TILDE);
        operators.put("^", TokenType.CARET);
        operators.put("^^", TokenType.CARETCARET);
        operators.put(">>", TokenType.GTGT);
        operators.put("<<", TokenType.LTLT);
        operators.put(">>>", TokenType.GTGTGT);

        operators.put("&&", TokenType.AMPAMP);
        operators.put("||", TokenType.BARBAR);

        OPERATORS = Map.copyOf(operators);
    }

    private static final Map<String, TokenType> KEYWORDS;

    static {
        final Map<String, TokenType> keywords = new HashMap<>();
        keywords.put("print", TokenType.PRINT);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);
        keywords.put("do", TokenType.DO);
        keywords.put("break", TokenType.BREAK);
        keywords.put("continue", TokenType.CONTINUE);
        keywords.put("def", TokenType.DEF);
        keywords.put("return", TokenType.RETURN);
        keywords.put("match", TokenType.MATCH);
        keywords.put("case", TokenType.CASE);
        keywords.put("class", TokenType.CLASS);
        keywords.put("new", TokenType.NEW);
        KEYWORDS = Map.copyOf(keywords);
    }

    public static List<Token> tokenize(String input) {
        return new Lexer(input).tokenize();
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
        this.buffer = new StringBuilder(40);
        row = col = 1;
    }

    public List<Token> tokenize() {
        while (pos < length) {
            // Fast path for skipping whitespaces
            while (Character.isWhitespace(peek(0))) {
                next();
            }
            final var current = peek(0);
            if (Character.isDigit(current)) tokenizeNumber();
            else if (isIdentifierStart(current)) tokenizeWorld();
            else if (current == '#') tokenizeHexNumber(1);
            else if (current == '"') tokenizeText();
            else if (OPERATION_CHARS.indexOf(current) != -1) {
                tokenizeOperation();
            } else {
                skip();
            }
        }
        return tokens;
    }

    private void tokenizeNumber() {
        final var buffer = createBuffer();
        final var startPos = markPos();
        char current = peek(0);
        if (current == '0' && (peek(1) == 'x' || (peek(1) == 'X'))) {
            tokenizeHexNumber(2);
            return;
        }
        boolean hasDot = false;
        while (true) {
            if (current == '.') {
                if (hasDot) throw error("Invalid float number");
                hasDot = true;
            } else if (current == 'e' || current == 'E') {
                int exp = subTokenizeScientificNumber();
                buffer.append(current).append(exp);
                break;
            } else if (!Character.isDigit(current))
                break;
            buffer.append(current);
            current = next();
        }
        addToken(TokenType.NUMBER, buffer.toString(), startPos);
    }

    private int subTokenizeScientificNumber() {
        int sign = 1;
        switch (next()) {
            case '-':
                sign = -1;
            case '+':
                skip();
                break;
        }

        boolean hasValue = false;
        char current = peek(0);
        while (current == '0') {
            hasValue = true;
            current = next();
        }
        int result = 0;
        int position = 0;
        while (Character.isDigit(current)) {
            result = result * 10 + (current - '0');
            current = next();
            position++;
        }
        if (position == 0 && !hasValue) throw error("Empty floating point exponent");
        if (position >= 4) {
            if (sign > 0) throw error("Float number too large");
            else throw error("Float number too small");
        }
        return sign * result;
    }

    private void tokenizeHexNumber(int skipChars) {
        final var buffer = createBuffer();
        final Pos startPos = markPos();
        // Skip HEX prefix 0x or #
        for (int i = 0; i < skipChars; i++) next();
        char current = peek(0);
        while (isHexNumber(current) || (current == '_')) {
            if (current != '_') {
                // allow _ symbol
                buffer.append(current);
            }
            current = next();
        }
        if (!buffer.isEmpty()) {
            addToken(TokenType.HEX_NUMBER, buffer.toString(), startPos);
        }
    }

    private static boolean isHexNumber(char current) {
        return Character.isDigit(current)
                || ('a' <= current && current <= 'f')
                || ('A' <= current && current <= 'F');
    }

    private void tokenizeOperation() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                tokenizeMultilineComment();
                return;
            }
        }
        final Pos startPos = markPos();
        final var buffer = createBuffer();
        while (true) {
            if (!buffer.isEmpty() && !OPERATORS.containsKey(buffer.toString() + current)) {
                addToken(OPERATORS.get(buffer.toString()), startPos);
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeWorld() {
        final var buffer = createBuffer();
        final Pos startPos = markPos();
        char current = peek(0);
        while (isIdentifierPart(current)) {
            buffer.append(current);
            current = next();
        }
        String word = buffer.toString();
        if (KEYWORDS.containsKey(word)) {
            addToken(KEYWORDS.get(word), startPos);
        } else {
            addToken(TokenType.WORD, word, startPos);
        }
    }

    private void tokenizeText() {
        final Pos startPos = markPos();
        skip();// Skip "
        final var buffer = createBuffer();
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
                    case 'u': // http://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.3
                        int rollbackPosition = pos;
                        while (current == 'u') current = next();
                        int escapedValue = 0;
                        for (int i = 12; i >= 0 && escapedValue != -1; i -= 4) {
                            if (isHexNumber(current)) {
                                escapedValue |= (Character.digit(current, 16) << i);
                            } else {
                                escapedValue = -1;
                            }
                            current = next();
                        }
                        if (escapedValue >= 0) {
                            buffer.append((char) escapedValue);
                        } else {
                            // rollback
                            buffer.append("\\u");
                            pos = rollbackPosition;
                        }
                        continue;
                }
                buffer.append('\\');
                continue;
            }
            if (current == '"') break;
            buffer.append(current);
            current = next();
        }
        skip(); // Skip "
        addToken(TokenType.TEXT, buffer.toString(), startPos);
    }

    private void tokenizeMultilineComment() {
        skip(); // /
        skip(); // *
        char current = peek(0);
        while (current != '*' || peek(1) != '/') {
            if (current == '\0') throw error("Missing close tag");
            current = next();
        }
        skip(); // *
        skip(); // /
    }

    private void tokenizeComment() {
        skip(); // /
        skip(); // /
        char current = peek(0);
        while ("\n\r\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void skip() {
        if (pos >= length) return;
        final char result = input.charAt(pos);
        if (result == '\n') {
            row++;
            col = 1;
        } else col++;
        pos++;
    }

    private char next() {
        skip();
        return peek(0);
    }

    private char peek(int relativePosition) {
        final var position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private Pos markPos() {
        return new Pos(row, col);
    }

    private LexerException error(String text) {
        return new LexerException(text, new Pos(row, col));
    }

    private StringBuilder createBuffer() {
        buffer.setLength(0);
        return buffer;
    }

    private void addToken(TokenType type, Pos startPos) {
        this.addToken(type, "", startPos);
    }

    private void addToken(TokenType type, String text, Pos startPos) {
        tokens.add(new Token(type, text, startPos));
    }

    private boolean isIdentifierStart(char current) {
        return (Character.isLetter(current) || (current == '_') || (current == '$'));
    }

    private boolean isIdentifierPart(char current) {
        return (Character.isLetterOrDigit(current) || (current == '_') || (current == '$'));
    }
}
