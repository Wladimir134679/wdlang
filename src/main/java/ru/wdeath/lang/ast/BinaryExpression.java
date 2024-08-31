package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Value;

public class BinaryExpression implements Expression {

    public enum Operator {
        ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"),
        REMAINDER("%"),

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
        if(expr1.eval() instanceof StringValue || expr1.eval() instanceof ArrayValue) {
            final String s1 = expr1.eval().asString();
            final var result = switch (operation) {
                case MULTIPLY -> {
                    int number = (int) expr2.eval().asDouble();
                    yield String.valueOf(s1).repeat(Math.max(0, number));
                }
                case ADD -> s1 + expr2.eval().asString();
                default -> s1 + expr2.eval().asString();
            };
            return new StringValue(result);
        }
        final double n1 = expr1.eval().asDouble();
        final double n2 = expr2.eval().asDouble();
        final var result = switch (operation) {
            case ADD -> n1 + n2;
            case SUBTRACT -> n1 - n2;
            case MULTIPLY -> n1 * n2;
            case DIVIDE -> n1 / n2;
            case AND -> (long)n1 & (long)n2;
            case OR -> (long)n1 | (long)n2;
            case XOR -> (long)n1 ^ (long)n2;
            case LSHIFT -> (long)n1 << (long)n2;
            case RSHIFT -> (long)n1 >> (long)n2;
            case URSHIFT -> (long)n1 >>> (long)n2;
            default -> throw new RuntimeException("Operation " + operation + " not supported");
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
