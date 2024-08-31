package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Value;

public class ConditionalExpression implements Expression {

    public enum Operator {
        PLUS, MINUS, MULTIPLY, DIVIDE,
        EQUALS, NOT_EQUALS,
        LT, LTEQ,
        GT, GTEQ,
        AND, OR,
    }

    private final Expression expr1, expr2;

    private final Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        double n1, n2;
        if (expr1.eval() instanceof StringValue) {
            n1 = expr1.eval().asString().compareTo(expr2.eval().asString());
            n2 = 0;
        } else {
            n1 = expr1.eval().asDouble();
            n2 = expr2.eval().asDouble();
        }
        final var result = switch (operation) {
            case EQUALS -> n1 == n2;
            case LT -> n1 < n2;
            case GT -> n1 > n2;
            case LTEQ -> n1 <= n2;
            case GTEQ -> n1 >= n2;
            case NOT_EQUALS -> n1 != n2;

            case AND -> (n1 != 0) && (n2 != 0);
            case OR -> (n1 != 0) || (n2 != 0);
            default -> n1 == n2;
        };
        return new NumberValue(result);
    }

    @Override
    public String toString() {
        return "BE{" +
                "e1=" + expr1 +
                ", o=" + operation +
                ", e2=" + expr2 +
                '}';
    }
}
