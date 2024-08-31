package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

public class UnaryExpression implements Expression {

    private final Expression expr;
    private final char operation;

    public UnaryExpression(char operation, Expression expr) {
        this.expr = expr;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        final var result = switch (operation) {
            case '+' -> expr.eval().asDouble();
            case '-' -> -expr.eval().asDouble();
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
        return new NumberValue(result);
    }

    @Override
    public String toString() {
        return "UE{" +
                "p=" + operation +
                ", e=" + expr +
                '}';
    }
}
