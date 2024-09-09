package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.OperationIsNotSupportedException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Types;
import ru.wdeath.lang.lib.Value;

public class ConditionalExpression implements Node {

    public enum Operator {
        EQUALS("=="),
        NOT_EQUALS("!="),

        LT("<"),
        LTEQ("<="),
        GT(">"),
        GTEQ(">="),

        AND("&&"),
        OR("||"),

        NULL_COALESCE("??");

        private final String name;

        Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public final Node expr1;
    public final Node expr2;
    public final Operator operation;

    public ConditionalExpression(Operator operation, Node expr1, Node expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        return switch (operation) {
            case AND -> NumberValue.fromBoolean((expr1AsInt() != 0) && (expr2AsInt() != 0));
            case OR -> NumberValue.fromBoolean((expr1AsInt() != 0) || (expr2AsInt() != 0));
            case NULL_COALESCE -> nullCoalesce();
            default -> NumberValue.fromBoolean(evalAndCompare());
        };
    }

    private boolean evalAndCompare() {
        final Value value1 = expr1.eval();
        final Value value2 = expr2.eval();

        double number1;
        double number2;
        if (value1.type() == Types.NUMBER) {
            number1 = value1.asDouble();
            number2 = value2.asDouble();
        } else {
            number1 = value1.compareTo(value2);
            number2 = 0;
        }

        return switch (operation) {
            case EQUALS -> number1 == number2;
            case NOT_EQUALS -> number1 != number2;
            case LT -> number1 < number2;
            case LTEQ -> number1 <= number2;
            case GT -> number1 > number2;
            case GTEQ -> number1 >= number2;
            default -> throw new OperationIsNotSupportedException(operation);
        };
    }

    private Value nullCoalesce() {
        Value value1;
        try {
            value1 = expr1.eval();
        } catch (NullPointerException npe) {
            value1 = null;
        }
        if (value1 == null) {
            return expr2.eval();
        }
        return value1;
    }

    private int expr1AsInt() {
        return expr1.eval().asInt();
    }

    private int expr2AsInt() {
        return expr2.eval().asInt();
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
        return "BE{" +
                "e1=" + expr1 +
                ", o=" + operation +
                ", e2=" + expr2 +
                '}';
    }
}
