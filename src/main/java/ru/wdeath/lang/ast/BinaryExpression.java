package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.OperationIsNotSupportedException;
import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.*;

public class BinaryExpression implements Expression {

    public enum Operator {
        ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"),
        REMAINDER("%"),
        PUSH("::"),

        AND("&"),
        OR("|"), // |
        XOR("^"), //^
        LSHIFT("<<"), // <<
        RSHIFT(">>"), // >>
        URSHIFT(">>>"); // >>>

        Operator(String name) {
            this.name = name;
        }

        final String name;
    }

    public final Expression expr1, expr2;

    public final Operator operation;

    public BinaryExpression(Operator operation, Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        final Value value1 = expr1.eval();
        final Value value2 = expr2.eval();

        if (value1.type() == Types.STRING) {
            return eval((StringValue) value1, value2);
        }
        if (value1.type() == Types.ARRAY) {
            return eval((ArrayValue) value1, value2);
        }
        return eval(value1, value2);
    }

    private Value eval(StringValue value1, Value value2){
        final String string1 = value1.asString();
        switch (operation) {
            case MULTIPLY: {
                final int iterations = (int) value2.asDouble();
                final StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < iterations; i++) {
                    buffer.append(string1);
                }
                return new StringValue(buffer.toString());
            }
            case ADD:
            default:
                return new StringValue(string1 + value2.asString());
        }
    }

    private Value eval(ArrayValue value1, Value value2) {
        switch (operation) {
            case LSHIFT:
                if (!(value2.type() == Types.ARRAY))
                    throw new TypeException("Cannot merge non array value to array");
                return ArrayValue.merge(value1, (ArrayValue) value2);
            case PUSH:
            default:
                return ArrayValue.add(value1, value2);
        }
    }
    private Value eval(Value value1, Value value2) {
        final double number1 = value1.asDouble();
        final double number2 = value2.asDouble();
        double result = switch (operation) {
            case ADD -> number1 + number2;
            case SUBTRACT -> number1 - number2;
            case MULTIPLY -> number1 * number2;
            case DIVIDE -> number1 / number2;
            case REMAINDER -> number1 % number2;

            // Bitwise
            case AND -> (int) number1 & (int) number2;
            case XOR -> (int) number1 ^ (int) number2;
            case OR -> (int) number1 | (int) number2;
            case LSHIFT -> (int) number1 << (int) number2;
            case RSHIFT -> (int) number1 >> (int) number2;
            case URSHIFT -> (int) number1 >>> (int) number2;
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
        return "BE{" +
                "e1=" + expr1 +
                ", o=" + operation.name +
                ", e2=" + expr2 +
                '}';
    }
}
