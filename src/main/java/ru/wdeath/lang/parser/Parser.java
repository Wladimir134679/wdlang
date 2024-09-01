package ru.wdeath.lang.parser;

import ru.wdeath.lang.ast.*;
import ru.wdeath.lang.exception.ParseException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.UserDefineFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private static final Token EOF = new Token(TokenType.EOF, "", 0, 0);

    private final List<Token> tokens;
    private final int size;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
        this.pos = 0;
    }

    public Statement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.addStatement(statement());
        }
        return result;
    }

    private Statement statementOrBlock() {
        if (peek(0).getType() == TokenType.LBRACE) return block();
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
            return new BreakStatement();
        if (match(TokenType.CONTINUE))
            return new ContinueStatement();
        if (match(TokenType.RETURN))
            return new ReturnStatement(expression());
        if (match(TokenType.MATCH))
            return new ExprStatement(match());
        if (peek(0).getType() == TokenType.WORD && peek(1).getType() == TokenType.LPAREN)
            return new ExprStatement(function(qualifiedName()));
        if (match(TokenType.DEF))
            return functionDefine();

        return assignmentStatement();
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
        if (lookMatch(foreachIndex, TokenType.WORD) && lookMatch(foreachIndex + 1, TokenType.COMMA)
                && lookMatch(foreachIndex + 2, TokenType.WORD) && lookMatch(foreachIndex + 3, TokenType.COLON)) {
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
        final String variable = consume(TokenType.WORD).getText();
        consume(TokenType.COLON);
        final Expression container = expression();
        if (openParen) consume(TokenType.RPAREN); // скобки
        final Statement statement = statementOrBlock();
        return new ForeachArrayStatement(variable, container, statement);
    }

    private ForeachMapStatement foreachMapStatement() {
        boolean openParen = match(TokenType.LPAREN); // необязательные скобки
        final String key = consume(TokenType.WORD).getText();
        consume(TokenType.COMMA);
        final String value = consume(TokenType.WORD).getText();
        consume(TokenType.COLON);
        final Expression container = expression();
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
        final Expression expression = expression();
        consume(TokenType.LBRACE);
        final List<MatchExpression.Pattern> patterns = new ArrayList<>();
        do {
            consume(TokenType.CASE);
            MatchExpression.Pattern pattern = null;
            final Token current = peek(0);
            if (match(TokenType.NUMBER)) {
                pattern = new MatchExpression.ConstantPattern(
                        new NumberValue(Double.parseDouble(current.getText()))
                );
            } else if (match(TokenType.HEX_NUMBER)) {
                pattern = new MatchExpression.ConstantPattern(
                        new NumberValue(Long.parseLong(current.getText(), 16))
                );
            } else if (match(TokenType.TEXT)) {
                pattern = new MatchExpression.ConstantPattern(
                        new StringValue(current.getText())
                );
            } else if (match(TokenType.WORD)) {
                pattern = new MatchExpression.VariablePattern(current.getText());
            }

            if (pattern == null) {
                throw new ParseException("Wrong pattern in match expression: " + current);
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

    private Statement assignmentStatement() {
        // WORD EQ
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.EQ)) {
            final String name = consume(TokenType.WORD).getText();
            consume(TokenType.EQ);
            return new AssignmentStatement(name, expression());
        }
        final Expression qualifiedNameExpr = qualifiedName();
        if (lookMatch(0, TokenType.EQ) && (qualifiedNameExpr instanceof ContainerAccessExpression)) {
            consume(TokenType.EQ);
            final ContainerAccessExpression containerExpr = (ContainerAccessExpression) qualifiedNameExpr;
            return new ContainerAssignmentStatement(containerExpr, expression());
        }
        throw new ParseException("Unknown statement " + peek(0));
    }


    private Expression function(Expression qualifiedName) {
//        final var name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        final var functionExpression = new FunctionExpression(qualifiedName);
        while (!match(TokenType.RPAREN)) {
            functionExpression.addArgument(expression());
            match(TokenType.COMMA);
        }
        return functionExpression;
    }

    private Expression array() {
        consume(TokenType.LBRACKET);
        final List<Expression> elements = new ArrayList<>();
        while (!match(TokenType.RBRACKET)) {
            elements.add(expression());
            match(TokenType.COMMA);
        }
        return new ArrayExpression(elements);
    }

    private Expression map() {
        consume(TokenType.LBRACE);
        final Map<Expression, Expression> elements = new HashMap<>();
        while (!match(TokenType.RBRACE)) {
            final Expression key = expression();
            consume(TokenType.COLON);
            final Expression value = expression();
            elements.put(key, value);
            match(TokenType.COMMA);
        }
        return new MapExpression(elements);
    }


    private FunctionDefineStatement functionDefine() {
        final var name = consume(TokenType.WORD).getText();
        consume(TokenType.LPAREN);
        final var argsName = new ArrayList<String>();
        while (!match(TokenType.RPAREN)) {
            argsName.add(consume(TokenType.WORD).getText());
            match(TokenType.COMMA);
        }
        if (lookMatch(0, TokenType.EQ)) {
            match(TokenType.EQ);
            return new FunctionDefineStatement(name, argsName, new ReturnStatement(expression()));
        }
        final var body = statementOrBlock();
        return new FunctionDefineStatement(name, argsName, body);
    }


    private Expression expression() {
        return ternary();
    }

    private Expression ternary() {
        Expression result = logicalOr();
        if (match(TokenType.QUESTION)) {
            final var expTrue = expression();
            consume(TokenType.COLON);
            final var expFalse = expression();
            return new TernaryExpression(result, expTrue, expFalse);
        }
        return result;
    }

    private Expression logicalOr() {
        Expression left = logicalAnd();
        while (true) {
            if (match(TokenType.BARBAR))
                left = new ConditionalExpression(ConditionalExpression.Operator.OR, left, logicalAnd());
            else break;
        }
        return left;
    }

    private Expression logicalAnd() {
        Expression left = bitwiseOr();
        while (true) {
            if (match(TokenType.AMPAMP))
                left = new ConditionalExpression(ConditionalExpression.Operator.AND, left, bitwiseOr());
            else break;
        }
        return left;
    }


    private Expression bitwiseOr() {
        Expression expression = bitwiseXor();

        while (true) {
            if (match(TokenType.BAR)) {
                expression = new BinaryExpression(BinaryExpression.Operator.OR, expression, bitwiseXor());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression bitwiseXor() {
        Expression expression = bitwiseAnd();

        while (true) {
            if (match(TokenType.CARET)) {
                expression = new BinaryExpression(BinaryExpression.Operator.XOR, expression, bitwiseAnd());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression bitwiseAnd() {
        Expression expression = equality();

        while (true) {
            if (match(TokenType.AMP)) {
                expression = new BinaryExpression(BinaryExpression.Operator.AND, expression, equality());
                continue;
            }
            break;
        }

        return expression;
    }

    private Expression equality() {
        Expression left = conditional();

        while (true) {
            if (match(TokenType.EQEQ))
                left = new ConditionalExpression(ConditionalExpression.Operator.EQUALS, left, conditional());
            else if (match(TokenType.EXCLEQ))
                left = new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUALS, left, conditional());
            else break;
        }
        return left;
    }

    private Expression conditional() {
        Expression expr = shift();
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


    private Expression shift() {
        Expression expression = additive();
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

    private Expression additive() {
        Expression expr = multiplicative();
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

    private Expression multiplicative() {
        Expression expr = unary();
        while (true) {
            if (match(TokenType.STAR))
                expr = new BinaryExpression(BinaryExpression.Operator.MULTIPLY, expr, unary());
            else if (match(TokenType.SLASH))
                expr = new BinaryExpression(BinaryExpression.Operator.DIVIDE, expr, unary());
            else if (match(TokenType.PERCENT))
                expr = new BinaryExpression(BinaryExpression.Operator.REMAINDER, expr, unary());
            else break;
        }
        return expr;
    }

    private Expression unary() {
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

    private Expression primary() {
        final var current = peek(0);
        if (match(TokenType.LPAREN)) {
            Expression expression = expression();
            match(TokenType.RPAREN);
            return expression;
        }
        if (match(TokenType.COLONCOLON)) {
            final String functionName = consume(TokenType.WORD).getText();
            return new FunctionReferenceExpression(functionName);
        }
        if (match(TokenType.MATCH))
            return match();
        if (match(TokenType.DEF)) {
            consume(TokenType.LPAREN);
            final var argsName = new ArrayList<String>();
            while (!match(TokenType.RPAREN)) {
                argsName.add(consume(TokenType.WORD).getText());
                match(TokenType.COMMA);
            }
            Statement statement;
            if (lookMatch(0, TokenType.EQ)) {
                match(TokenType.EQ);
                statement = new ReturnStatement(expression());
            } else
                statement = statementOrBlock();
            return new ValueExpression(new UserDefineFunction(argsName, statement));
        }
        return variable();
    }

    private Expression variable() {
        final var current = peek(0);
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.LPAREN)) {
            return function(new ValueExpression(consume(TokenType.WORD).getText()));
        }
        final Expression qualifiedNameExpr = qualifiedName();
        if (qualifiedNameExpr != null) {
            // variable(args) || arr["key"](args) || obj.key(args)
            if (lookMatch(0, TokenType.LPAREN)) {
                return function(qualifiedNameExpr);
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

    private Expression value() {
        final var current = peek(0);
        if (match(TokenType.TEXT))
            return new ValueExpression(current.getText());
        if (match(TokenType.NUMBER))
            return new ValueExpression(Double.parseDouble(current.getText()));
        if (match(TokenType.HEX_NUMBER))
            return new ValueExpression(Long.parseLong(current.getText(), 16));
        throw new ParseException("unknown expression " + current);
    }

    private Expression qualifiedName() {
        final Token current = peek(0);
        if (!match(TokenType.WORD)) return null;

        final List<Expression> indices = variableSuffix();
        if ((indices == null) || indices.isEmpty()) {
            return new VariableExpression(current.getText());
        }
        return new ContainerAccessExpression(current.getText(), indices);
    }

    private List<Expression> variableSuffix() {
        // .key1.arr1[expr1][expr2].key2
        if (!lookMatch(0, TokenType.DOT) && !lookMatch(0, TokenType.LBRACKET)) {
            return null;
        }
        final List<Expression> indices = new ArrayList<>();
        while (lookMatch(0, TokenType.DOT) || lookMatch(0, TokenType.LBRACKET)) {
            if (match(TokenType.DOT)) {
                final String fieldName = consume(TokenType.WORD).getText();
                final Expression key = new ValueExpression(fieldName);
                indices.add(key);
            }
            if (match(TokenType.LBRACKET)) {
                indices.add(expression());
                consume(TokenType.RBRACKET);
            }
        }
        return indices;
    }

    private Token consume(TokenType type) {
        Token current = peek(0);
        if (type != current.getType())
            throw new ParseException("Token " + current + " does not match expected " + type);
        pos++;
        return current;
    }

    private boolean lookMatch(int pos, TokenType type) {
        return peek(pos).getType() == type;
    }

    private boolean match(TokenType type) {
        Token current = peek(0);
        if (type != current.getType()) return false;
        pos++;
        return true;
    }

    private Token peek(int relativePosition) {
        final var position = pos + relativePosition;
        if (position >= size) return EOF;
        return tokens.get(position);
    }
}
