package ru.wdeath.lang.parser;

import ru.wdeath.lang.ast.*;
import ru.wdeath.lang.exception.ParseException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.UserDefinedFunction;
import ru.wdeath.lang.parser.error.ParseError;
import ru.wdeath.lang.parser.error.ParseErrors;
import ru.wdeath.lang.utils.Pos;
import ru.wdeath.lang.utils.Range;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "", new Pos(0, 0));


    private static final EnumMap<TokenType, BinaryExpression.Operator> assignOperator;

    static {
        assignOperator = new EnumMap<>(TokenType.class);
        assignOperator.put(TokenType.EQ, null);
        assignOperator.put(TokenType.PLUSEQ, BinaryExpression.Operator.ADD);
        assignOperator.put(TokenType.MINUSEQ, BinaryExpression.Operator.SUBTRACT);
        assignOperator.put(TokenType.STAREQ, BinaryExpression.Operator.MULTIPLY);
        assignOperator.put(TokenType.SLASHEQ, BinaryExpression.Operator.DIVIDE);
        assignOperator.put(TokenType.PERCENTEQ, BinaryExpression.Operator.REMAINDER);
        assignOperator.put(TokenType.AMPEQ, BinaryExpression.Operator.AND);
        assignOperator.put(TokenType.CARETEQ, BinaryExpression.Operator.XOR);
        assignOperator.put(TokenType.BAREQ, BinaryExpression.Operator.OR);
        assignOperator.put(TokenType.COLONCOLONEQ, BinaryExpression.Operator.PUSH);
        assignOperator.put(TokenType.LTLTEQ, BinaryExpression.Operator.LSHIFT);
        assignOperator.put(TokenType.GTGTEQ, BinaryExpression.Operator.RSHIFT);
        assignOperator.put(TokenType.GTGTGTEQ, BinaryExpression.Operator.URSHIFT);
    }

    private final List<Token> tokens;
    private final int size;
    private int index;
    private final ParseErrors parseErrors;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
        this.index = 0;
        parseErrors = new ParseErrors();
    }

    public ParseErrors getParseErrors() {
        return parseErrors;
    }

    public Statement parse() {
        parseErrors.clear();
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            try {
                result.addStatement(statement());
            } catch (ParseException ex) {
                parseErrors.add(new ParseError(ex.getMessage(), ex.getRange()));
                recover();
            } catch (Exception ex) {
                parseErrors.add(new ParseError(ex.getMessage(), getRange(), List.of(ex.getStackTrace())));
                recover();
            }
        }
        return result;
    }

    private void recover() {
        int preRecoverIndex = index;
        for (int i = preRecoverIndex; i <= size; i++) {
            index = i;
            try {
                statement();
                // successfully parsed,
                index = i; // restore position
                return;
            } catch (Exception ex) {
                // fail
            }
        }
    }

    private Statement statementOrBlock() {
        if (peek(0).type() == TokenType.LBRACE) return block();
        return statement();
    }

    private Statement block() {
        BlockStatement block = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            block.addStatement(statement());
        }
        return block;
    }

    private Statement statement() {
        if (match(TokenType.PRINT))
            return new PrintStatement(expression());
        if (match(TokenType.IF))
            return isElse();
        if (match(TokenType.WHILE))
            return whileStatement();
        if (match(TokenType.DO))
            return doWhileStatement();
        if (match(TokenType.FOR))
            return forStatement();
        if (match(TokenType.BREAK))
            return new BreakStatement(getRange(index - 1, index - 1));
        if (match(TokenType.CONTINUE))
            return new ContinueStatement(getRange(index - 1, index - 1));
        if (match(TokenType.RETURN))
            return new ReturnStatement(expression());
        if (match(TokenType.MATCH))
            return match();
        if (match(TokenType.CLASS))
            return classDeclaration();
        if (peek(0).type() == TokenType.WORD && peek(1).type() == TokenType.LPAREN)
            return functionCallStatement();
        if (match(TokenType.DEF))
            return functionDefine();

        return assignmentStatement();
    }

    private ExprStatement functionCallStatement() {
        return new ExprStatement(
                functionChain(new ValueExpression(consume(TokenType.WORD).text()))
        );
    }

    private Statement whileStatement() {
        final var condition = expression();
        final var statement = statementOrBlock();
        return new WhileStatement(condition, statement);
    }

    private Statement doWhileStatement() {
        final var statement = statementOrBlock();
        consume(TokenType.WHILE);
        final var condition = expression();
        return new DoWhileStatement(condition, statement);
    }

    private Statement forStatement() {
        int foreachIndex = lookMatch(0, TokenType.LPAREN) ? 1 : 0;
        if (lookMatch(foreachIndex, TokenType.WORD) && lookMatch(foreachIndex + 1, TokenType.COLON)) {
            // for v : arr || for (v : arr)
            return foreachArrayStatement();
        }
        if (lookMatch(foreachIndex, TokenType.WORD)
                && lookMatch(foreachIndex + 1, TokenType.COMMA)
                && lookMatch(foreachIndex + 2, TokenType.WORD)
                && lookMatch(foreachIndex + 3, TokenType.COLON)) {
            // for key, value : arr || for (key, value : arr)
            return foreachMapStatement();
        }

        boolean openParen = match(TokenType.LPAREN); // необязательные скобки
        final var init = assignmentStatement();
        consume(TokenType.COMMA);
        final var termination = expression();
        consume(TokenType.COMMA);
        final var increment = assignmentStatement();
        if (openParen) consume(TokenType.RPAREN); // скобки
        final var body = statementOrBlock();
        return new ForStatement(init, termination, increment, body);
    }

    private ForeachArrayStatement foreachArrayStatement() {
        boolean openParen = match(TokenType.LPAREN); // необязательные скобки
        final String variable = consume(TokenType.WORD).text();
        consume(TokenType.COLON);
        final Node container = expression();
        if (openParen) consume(TokenType.RPAREN); // скобки
        final Statement statement = statementOrBlock();
        return new ForeachArrayStatement(variable, container, statement);
    }

    private ForeachMapStatement foreachMapStatement() {
        boolean openParen = match(TokenType.LPAREN); // необязательные скобки
        final String key = consume(TokenType.WORD).text();
        consume(TokenType.COMMA);
        final String value = consume(TokenType.WORD).text();
        consume(TokenType.COLON);
        final Node container = expression();
        if (openParen) consume(TokenType.RPAREN); // скобки
        final Statement statement = statementOrBlock();
        return new ForeachMapStatement(key, value, container, statement);
    }

    private Statement isElse() {
        final var condition = expression();
        final var ifStatement = statementOrBlock();
        Statement elseStatement = null;
        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        }
        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private MatchExpression match() {
        // match expression {
        //  case pattern1: result1
        //  case pattern2 if extr: result2
        // }
        final Node expression = expression();
        consume(TokenType.LBRACE);
        final List<MatchExpression.Pattern> patterns = new ArrayList<>();
        do {
            consume(TokenType.CASE);
            MatchExpression.Pattern pattern = null;
            final Token current = peek(0);
            if (match(TokenType.NUMBER)) {
                pattern = new MatchExpression.ConstantPattern(
                        NumberValue.of(Double.parseDouble(current.text()))
                );
            } else if (match(TokenType.DECIMAL_NUMBER)) {
                // case 0.5:
                pattern = new MatchExpression.ConstantPattern(
                        NumberValue.of(createDecimalNumber(current.text()))
                );
            } else if (match(TokenType.HEX_NUMBER)) {
                pattern = new MatchExpression.ConstantPattern(
                        NumberValue.of(Long.parseLong(current.text(), 16))
                );
            } else if (match(TokenType.TEXT)) {
                pattern = new MatchExpression.ConstantPattern(
                        new StringValue(current.text())
                );
            } else if (match(TokenType.WORD)) {
                pattern = new MatchExpression.VariablePattern(current.text());
            }

            if (pattern == null) {
                throw error("Wrong pattern in match expression: " + current);
            }
            if (match(TokenType.IF)) {
                pattern.optCondition = expression();
            }

            consume(TokenType.COLON);
            if (lookMatch(0, TokenType.LBRACE)) {
                pattern.result = block();
            } else {
                pattern.result = new ReturnStatement(expression());
            }
            patterns.add(pattern);
        } while (!match(TokenType.RBRACE));

        return new MatchExpression(expression, patterns);
    }

    private Statement classDeclaration() {
        // class Name {
        //   x = 123
        //   str = ""
        //   def method() = str
        // }
        final String name = consume(TokenType.WORD).text();
        final ClassDeclarationStatement classDeclaration = new ClassDeclarationStatement(name);
        consume(TokenType.LBRACE);
        do {
            if (match(TokenType.DEF)) {
                classDeclaration.addMethod(functionDefine());
            } else {
                final AssignmentExpression fieldDeclaration = assignmentStrict();
                if (fieldDeclaration != null) {
                    classDeclaration.addField(fieldDeclaration);
                } else {
                    throw error("Class can contain only assignments and function declarations");
                }
            }
        } while (!match(TokenType.RBRACE));
        return classDeclaration;
    }

    private Statement assignmentStatement() {
        final Node expression = expression();
        if (expression instanceof Statement) {
            return (Statement) expression;
        }
        throw error("Unknown statement: " + peek(0));
    }


    private FunctionExpression function(Node qualifiedName) {
        final var startTokenIndex = index - 1;
        consume(TokenType.LPAREN);
        final var functionExpression = new FunctionExpression(qualifiedName);
        while (!match(TokenType.RPAREN)) {
            functionExpression.addArgument(expression());
            match(TokenType.COMMA);
        }
        functionExpression.setRange(getRange(startTokenIndex, index - 1));
        return functionExpression;
    }

    private Node functionChain(Node qualifiedNameExpr) {
        // f1()()() || f1().f2().f3() || f1().key
        final Node expr = function(qualifiedNameExpr);
        if (lookMatch(0, TokenType.LPAREN)) {
            return functionChain(expr);
        }
        if (lookMatch(0, TokenType.DOT)) {
            final List<Node> indices = variableSuffix();
            if (indices.isEmpty()) return expr;

            if (lookMatch(0, TokenType.LPAREN)) {
                // next function call
                return functionChain(new ContainerAccessExpression(expr, indices, getRange()));
            }
            // container access
            return new ContainerAccessExpression(expr, indices, getRange());
        }
        return expr;
    }

    private Node array() {
        consume(TokenType.LBRACKET);
        final List<Node> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }

    private Node map() {
        consume(TokenType.LBRACE);
        final Map<Node, Node> elements = new HashMap<>();
        while (!match(TokenType.RBRACE)) {
            final Node key = expression();
            consume(TokenType.COLON);
            final Node value = expression();
            elements.put(key, value);
            match(TokenType.COMMA);
        }
        return new MapExpression(elements);
    }


    private FunctionDefineStatement functionDefine() {
        final var startTokenIndex = index - 1;
        final String name = consume(TokenType.WORD).text();
        final Arguments arguments = arguments();
        final Statement body = statementBody();
        return new FunctionDefineStatement(name, arguments, body, getRange(startTokenIndex, index - 1));
    }


    private Arguments arguments() {
        // (arg1, arg2, arg3 = expr1, arg4 = expr2)
        final Arguments arguments = new Arguments();
        boolean startsOptionalArgs = false;
        final var startTokenIndex = index;
        consume(TokenType.LPAREN);
        while (!match(TokenType.RPAREN)) {
            final String name = consume(TokenType.WORD).text();
            if (match(TokenType.EQ)) {
                startsOptionalArgs = true;
                arguments.addOptional(name, variable());
            } else if (!startsOptionalArgs) {
                arguments.addRequired(name);
            } else {
                throw error(errorRequiredArgumentAfterOptional());
            }
            match(TokenType.COMMA);
        }
        arguments.setRange(getRange(startTokenIndex, index - 1));
        return arguments;
    }


    private Statement statementBody() {
        if (match(TokenType.EQ)) {
            return new ReturnStatement(expression());
        }
        return statementOrBlock();
    }


    private Node expression() {
        return assignment();
    }

    private Node assignment() {
        final Node assignment = assignmentStrict();
        if (assignment != null) {
            return assignment;
        }
        return ternary();
    }

    private AssignmentExpression assignmentStrict() {
        final int position = index;
        final Node targetExpr = qualifiedName();
        if (!(targetExpr instanceof Accessible)) {
            index = position;
            return null;
        }
        final TokenType currentType = peek(0).type();
        if (!assignOperator.containsKey(currentType)) {
            index = position;
            return null;
        }
        match(currentType);

        final BinaryExpression.Operator op = assignOperator.get(currentType);
        final Node expression = expression();

        return new AssignmentExpression(op, (Accessible) targetExpr, expression, getRange());
    }

    private Node ternary() {
        Node result = logicalOr();
        if (match(TokenType.QUESTION)) {
            final var expTrue = expression();
            consume(TokenType.COLON);
            final var expFalse = expression();
            return new TernaryExpression(result, expTrue, expFalse);
        }
        return result;
    }

    private Node logicalOr() {
        Node left = logicalAnd();
        while (true) {
            if (match(TokenType.BARBAR))
                left = new ConditionalExpression(ConditionalExpression.Operator.OR, left, logicalAnd());
            else break;
        }
        return left;
    }

    private Node logicalAnd() {
        Node left = bitwiseOr();
        while (true) {
            if (match(TokenType.AMPAMP))
                left = new ConditionalExpression(ConditionalExpression.Operator.AND, left, bitwiseOr());
            else break;
        }
        return left;
    }


    private Node bitwiseOr() {
        Node expression = bitwiseXor();

        while (true) {
            if (match(TokenType.BAR)) {
                expression = new BinaryExpression(BinaryExpression.Operator.OR, expression, bitwiseXor());
                continue;
            }
            break;
        }

        return expression;
    }

    private Node bitwiseXor() {
        Node expression = bitwiseAnd();

        while (true) {
            if (match(TokenType.CARET)) {
                expression = new BinaryExpression(BinaryExpression.Operator.XOR, expression, bitwiseAnd());
                continue;
            }
            break;
        }

        return expression;
    }

    private Node bitwiseAnd() {
        Node expression = equality();

        while (true) {
            if (match(TokenType.AMP)) {
                expression = new BinaryExpression(BinaryExpression.Operator.AND, expression, equality());
                continue;
            }
            break;
        }

        return expression;
    }

    private Node equality() {
        Node left = conditional();

        while (true) {
            if (match(TokenType.EQEQ))
                left = new ConditionalExpression(ConditionalExpression.Operator.EQUALS, left, conditional());
            else if (match(TokenType.EXCLEQ))
                left = new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, left, conditional());
            else break;
        }
        return left;
    }

    private Node conditional() {
        Node expr = shift();
        while (true) {
            if (match(TokenType.LT))
                expr = new ConditionalExpression(ConditionalExpression.Operator.LT, expr, shift());
            else if (match(TokenType.LTEQ))
                expr = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, expr, shift());
            else if (match(TokenType.GT))
                expr = new ConditionalExpression(ConditionalExpression.Operator.GT, expr, shift());
            else if (match(TokenType.GTEQ))
                expr = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, expr, shift());
            else break;
        }
        return expr;
    }


    private Node shift() {
        Node expression = additive();
        while (true) {
            if (match(TokenType.LTLT))
                expression = new BinaryExpression(BinaryExpression.Operator.LSHIFT, expression, additive());
            else if (match(TokenType.GTGT))
                expression = new BinaryExpression(BinaryExpression.Operator.RSHIFT, expression, additive());
            else if (match(TokenType.GTGTGT))
                expression = new BinaryExpression(BinaryExpression.Operator.URSHIFT, expression, additive());
            else
                break;
        }
        return expression;
    }

    private Node additive() {
        Node expr = multiplicative();
        while (true) {
            if (match(TokenType.PLUS))
                expr = new BinaryExpression(BinaryExpression.Operator.ADD, expr, multiplicative());
            else if (match(TokenType.MINUS))
                expr = new BinaryExpression(BinaryExpression.Operator.SUBTRACT, expr, multiplicative());
            else if (match(TokenType.COLONCOLON))
                expr = new BinaryExpression(BinaryExpression.Operator.PUSH, expr, multiplicative());
            else break;
        }
        return expr;
    }

    private Node multiplicative() {
        Node expr = objectCreation();
        while (true) {
            if (match(TokenType.STAR))
                expr = new BinaryExpression(BinaryExpression.Operator.MULTIPLY, expr, objectCreation());
            else if (match(TokenType.SLASH))
                expr = new BinaryExpression(BinaryExpression.Operator.DIVIDE, expr, objectCreation());
            else if (match(TokenType.PERCENT))
                expr = new BinaryExpression(BinaryExpression.Operator.REMAINDER, expr, objectCreation());
            else break;
        }
        return expr;
    }

    private Node objectCreation() {
        if (match(TokenType.NEW)) {
            final var startTokenIndex = index - 1;
            final String className = consume(TokenType.WORD).text();
            final List<Node> args = new ArrayList<>();
            consume(TokenType.LPAREN);
            while (!match(TokenType.RPAREN)) {
                args.add(expression());
                match(TokenType.COMMA);
            }
            return new ObjectCreationExpression(className, args, getRange(startTokenIndex, index - 1));
        }

        return unary();
    }

    private Node unary() {
        if (match(TokenType.PLUSPLUS))
            return new UnaryExpression(UnaryExpression.Operator.INCREMENT_PREFIX, primary());
        if (match(TokenType.MINUSMINUS))
            return new UnaryExpression(UnaryExpression.Operator.DECREMENT_PREFIX, primary());
        if (match(TokenType.MINUS))
            return new UnaryExpression(UnaryExpression.Operator.NEGATE, primary());
        if (match(TokenType.EXCL))
            return new UnaryExpression(UnaryExpression.Operator.NOT, primary());
        if (match(TokenType.TILDE))
            return new UnaryExpression(UnaryExpression.Operator.COMPLEMENT, primary());
        if (match(TokenType.PLUS))
            primary();
        return primary();
    }

    private Node primary() {
        if (match(TokenType.LPAREN)) {
            Node expression = expression();
            match(TokenType.RPAREN);
            return expression;
        }
        if (match(TokenType.COLONCOLON)) {
            final String functionName = consume(TokenType.WORD).text();
            return new FunctionReferenceExpression(functionName);
        }
        if (match(TokenType.MATCH))
            return match();
        if (match(TokenType.DEF)) {
            final var startTokenIndex = index - 1;
            final Arguments arguments = arguments();
            final Statement statement = statementBody();
            Range range = getRange(startTokenIndex, index - 1);
            return new ValueExpression(new UserDefinedFunction(arguments, statement, range));
        }
        return variable();
    }

    private Node variable() {
        // function(...
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAREN)) {
            return functionChain(new ValueExpression(consume(TokenType.WORD).text()));
        }

        final Node qualifiedNameExpr = qualifiedName();
        if (qualifiedNameExpr != null) {
            // variable(args) || arr["key"](args) || obj.key(args)
            if (lookMatch(0, TokenType.LPAREN)) {
                return functionChain(qualifiedNameExpr);
            }
            // postfix increment/decrement
            if (match(TokenType.PLUSPLUS)) {
                return new UnaryExpression(UnaryExpression.Operator.INCREMENT_POSTFIX, qualifiedNameExpr);
            }
            if (match(TokenType.MINUSMINUS)) {
                return new UnaryExpression(UnaryExpression.Operator.DECREMENT_POSTFIX, qualifiedNameExpr);
            }
            return qualifiedNameExpr;
        }

        if (lookMatch(0, TokenType.LBRACKET)) {
            return array();
        }
        if (lookMatch(0, TokenType.LBRACE)) {
            return map();
        }
        return value();
    }

    private Node value() {
        final var current = peek(0);
        if (match(TokenType.TEXT)) {
            final ValueExpression strExpr = new ValueExpression(current.text());
            // "text".property || "text".func()
            if (lookMatch(0, TokenType.DOT)) {
                if (lookMatch(1, TokenType.WORD) && lookMatch(2, TokenType.LPAREN)) {
                    match(TokenType.DOT);
                    return functionChain(new ContainerAccessExpression(
                            strExpr, Collections.singletonList(
                            new ValueExpression(consume(TokenType.WORD).text())
                    ), getRange()));
                }
                final List<Node> indices = variableSuffix();
                if (indices.isEmpty()) {
                    return strExpr;
                }
                return new ContainerAccessExpression(strExpr, indices, getRange());
            }
            return strExpr;
        }
        if (match(TokenType.DECIMAL_NUMBER)) {
            return new ValueExpression(createDecimalNumber(current.text()));
        }
        if (match(TokenType.NUMBER))
            return new ValueExpression(createNumber(current.text(), 10));
        if (match(TokenType.HEX_NUMBER))
            return new ValueExpression(createNumber(current.text(), 16));
        throw error("Unknown expression: " + current);
    }

    private Node qualifiedName() {
        final Token current = peek(0);
        final var startTokenIndex = index;
        if (!match(TokenType.WORD)) return null;

        final List<Node> indices = variableSuffix();
        if (indices.isEmpty()) {
            final var variable = new VariableExpression(current.text());
            variable.setRange(getRange(startTokenIndex, index - 1));
            return variable;
        }
        return new ContainerAccessExpression(current.text(), indices, getRange());
    }

    private List<Node> variableSuffix() {
        // .key1.arr1[expr1][expr2].key2
        if (!lookMatch(0, TokenType.DOT) && !lookMatch(0, TokenType.LBRACKET)) {
            return Collections.emptyList();
        }
        final List<Node> indices = new ArrayList<>();
        while (lookMatch(0, TokenType.DOT) || lookMatch(0, TokenType.LBRACKET)) {
            if (match(TokenType.DOT)) {
                final String fieldName = consume(TokenType.WORD).text();
                final Node key = new ValueExpression(fieldName);
                indices.add(key);
            }
            if (match(TokenType.LBRACKET)) {
                indices.add(expression());
                consume(TokenType.RBRACKET);
            }
        }
        return indices;
    }

    private Number createNumber(String text, int radix) {
        // Double
        if (text.contains(".") || text.contains("e") || text.contains("E")) {
            return Double.parseDouble(text);
        }
        // Integer
        try {
            return Integer.parseInt(text, radix);
        } catch (NumberFormatException nfe) {
            return Long.parseLong(text, radix);
        }
    }

    private Number createDecimalNumber(String text) {
        // Double
        return Double.parseDouble(text);
    }

    private Token consume(TokenType expectedType) {
        final Token actual = peek(0);
        if (expectedType != actual.type()) {
            throw error(errorUnexpectedToken(actual, expectedType));
        }
        index++;
        return actual;
    }

    private Token consumeOrExplainError(TokenType expectedType, Function<Token, String> errorMessageFunction) {
        final Token actual = peek(0);
        if (expectedType != actual.type()) {
            throw error(errorUnexpectedToken(actual, expectedType)
                    + errorMessageFunction.apply(actual));
        }
        index++;
        return actual;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return peek(pos).type() == type;
    }

    private boolean match(TokenType type) {
        Token current = peek(0);
        if (type != current.type()) return false;
        index++;
        return true;
    }

    private Token peek(int relativePosition) {
        final var position = index + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }

    private Range getRange() {
        return getRange(index, index);
    }

    private Range getRange(int startIndex, int endIndex) {
        if (size == 0) return Range.ZERO;
        final int last = size - 1;
        Pos start = tokens.get(Math.min(startIndex, last)).pos();
        if (startIndex == endIndex) {
            return new Range(start, start);
        } else {
            Pos end = tokens.get(Math.min(endIndex, last)).pos();
            return new Range(start, end);
        }
    }

    private ParseException error(String message) {
        return new ParseException(message, getRange());
    }

    private ParseException error(String message, int startIndex, int endIndex) {
        return new ParseException(message, getRange(startIndex, endIndex));
    }

    private static String errorUnexpectedToken(Token actual, TokenType expectedType) {
        return "Expected token with type " + expectedType + ", but found " + actual.shortDescription();
    }

    private static String errorUnexpectedTokens(Token actual, TokenType... expectedTypes) {
        String tokenTypes = Arrays.stream(expectedTypes).map(Enum::toString).collect(Collectors.joining(", "));
        return "Expected tokens with types one of " + tokenTypes + ", but found " + actual.shortDescription();
    }

    private static String errorDestructuringAssignmentEmpty() {
        return "Destructuring assignment should contain at least one variable name to assign." +
                "\nCorrect syntax: extract(v1, , , v4) = ";
    }

    private static String errorRequiredArgumentAfterOptional() {
        return "Required argument cannot be placed after optional.";
    }

    private static String explainUseStatementError(Token current) {
        String example = current.type().equals(TokenType.TEXT)
                ? "use " + current.text()
                : "use std, math";
        return "\nNote: as of OwnLang 2.0.0 use statement simplifies modules list syntax. " +
                "Correct syntax: " + example;
    }
}
