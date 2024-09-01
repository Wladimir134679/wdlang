package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.OperationIsNotSupportedException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

public class UnaryExpression implements Expression {

    public final Expression expr;
    public final Operator operation;

    public enum Operator {
        INCREMENT_PREFIX("++"),
        DECREMENT_PREFIX("--"),
        INCREMENT_POSTFIX("++"),
        DECREMENT_POSTFIX("--"),
        NEGATE("-"),
        // Boolean
        NOT("!"),
        // Bitwise
        COMPLEMENT("~");

        private final String name;

        private Operator(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public UnaryExpression(Operator operation, Expression expr) {
        this.expr = expr;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        final var result = switch (operation) {
            case NEGATE -> -expr.eval().asDouble();
            case COMPLEMENT -> ~(int) expr.eval().asDouble();
            case NOT -> expr.eval().asDouble() != 0 ? 0 : 1;
            default -> throw new OperationIsNotSupportedException(operation);
        };
        return new NumberValue(result);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "UE{" +
                "p=" + operation +
                ", e=" + expr +
                '}';
    }
}
