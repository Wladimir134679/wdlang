package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.OperationIsNotSupportedException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Types;
import ru.wdeath.lang.lib.Value;

public class UnaryExpression implements Expression, Statement {

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
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        Value value = expr.eval();
        switch (operation) {
            case INCREMENT_PREFIX: {
                if (expr instanceof Accessible expr1) {
                    return expr1.set(increment(value));
                }
                return increment(value);
            }
            case DECREMENT_PREFIX: {
                if (expr instanceof Accessible expr1) {
                    return expr1.set(decrement(value));
                }
                return decrement(value);
            }
            case INCREMENT_POSTFIX: {
                if (expr instanceof Accessible expr1) {
                    expr1.set(increment(value));
                    return value;
                }
                return increment(value);
            }
            case DECREMENT_POSTFIX: {
                if (expr instanceof Accessible expr1) {
                    expr1.set(decrement(value));
                    return value;
                }
                return decrement(value);
            }
            case NEGATE:
                return negate(value);
            case COMPLEMENT:
                return complement(value);
            case NOT:
                return not(value);
            default:
                throw new OperationIsNotSupportedException(operation);
        }
    }

    private Value increment(Value value) {
        if (value.type() == Types.NUMBER) {
            final Number number = ((NumberValue) value).raw();
            if (number instanceof Double) {
                return NumberValue.of(number.doubleValue() + 1);
            }
            if (number instanceof Float) {
                return NumberValue.of(number.floatValue() + 1);
            }
            if (number instanceof Long) {
                return NumberValue.of(number.longValue() + 1);
            }
        }
        return NumberValue.of(value.asInt() + 1);
    }

    private Value decrement(Value value) {
        if (value.type() == Types.NUMBER) {
            final Number number = ((NumberValue) value).raw();
            if (number instanceof Double) {
                return NumberValue.of(number.doubleValue() - 1);
            }
            if (number instanceof Float) {
                return NumberValue.of(number.floatValue() - 1);
            }
            if (number instanceof Long) {
                return NumberValue.of(number.longValue() - 1);
            }
        }
        return NumberValue.of(value.asInt() - 1);
    }

    private Value negate(Value value) {
        if (value.type() == Types.STRING) {
            final StringBuilder sb = new StringBuilder(value.asString());
            return new StringValue(sb.reverse().toString());
        }
        if (value.type() == Types.NUMBER) {
            final Number number = ((NumberValue) value).raw();
            if (number instanceof Double) {
                return NumberValue.of(-number.doubleValue());
            }
            if (number instanceof Float) {
                return NumberValue.of(-number.floatValue());
            }
            if (number instanceof Long) {
                return NumberValue.of(-number.longValue());
            }
        }
        return NumberValue.of(-value.asInt());
    }

    private Value complement(Value value) {
        if (value.type() == Types.NUMBER) {
            final Number number = ((NumberValue) value).raw();
            if (number instanceof Long) {
                return NumberValue.of(~number.longValue());
            }
        }
        return NumberValue.of(~value.asInt());
    }

    private Value not(Value value) {
        return NumberValue.fromBoolean(value.asInt() == 0);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
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
