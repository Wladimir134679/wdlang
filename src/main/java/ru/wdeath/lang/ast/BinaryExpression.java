package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Value;

public class BinaryExpression implements Expression {

    private final Expression expr1, expr2;

    private final char operation;

    public BinaryExpression(char operation, Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        if(expr1.eval() instanceof StringValue || expr1.eval() instanceof ArrayValue) {
            final String s1 = expr1.eval().asString();
            final var result = switch (operation) {
                case '*' -> {
                    int number = (int) expr2.eval().asDouble();
                    yield String.valueOf(s1).repeat(Math.max(0, number));
                }
                case '+' -> s1 + expr2.eval().asString();
                default -> s1 + expr2.eval().asString();
            };
            return new StringValue(result);
        }
        final double n1 = expr1.eval().asDouble();
        final double n2 = expr2.eval().asDouble();
        final var result = switch (operation) {
            case '+' -> n1 + n2;
            case '-' -> n1 - n2;
            case '*' -> n1 * n2;
            case '/' -> n1 / n2;
            default -> n1 + n2;
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
