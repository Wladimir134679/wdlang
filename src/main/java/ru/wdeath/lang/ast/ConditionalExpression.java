package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.OperationIsNotSupportedException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Types;
import ru.wdeath.lang.lib.Value;

public class ConditionalExpression implements Expression {

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

    public final Expression expr1, expr2;

    public final Operator operation;

    public ConditionalExpression(Operator operation, Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.operation = operation;
    }

    @Override
    public Value eval() {
        final Value value1 = expr1.eval();
        switch (operation) {
            case AND:
                return NumberValue.fromBoolean(
                        (value1.asInt() != 0) && (expr2.eval().asInt() != 0));
            case OR:
                return NumberValue.fromBoolean(
                        (value1.asInt() != 0) || (expr2.eval().asInt() != 0));
        }


        final Value value2 = expr2.eval();

        double number1, number2;

        if (value1.type() == Types.NUMBER) {
            number1 = value1.asDouble();
            number2 = value2.asDouble();
        } else {
            number1 = value1.compareTo(value2);
            number2 = 0;
        }

        boolean result;
        switch (operation) {
            case EQUALS:
                result = number1 == number2;
                break;
            case NOT_EQUALS:
                result = number1 != number2;
                break;

            case LT:
                result = number1 < number2;
                break;
            case LTEQ:
                result = number1 <= number2;
                break;
            case GT:
                result = number1 > number2;
                break;
            case GTEQ:
                result = number1 >= number2;
                break;

            default:
                throw new OperationIsNotSupportedException(operation);
        }
        return NumberValue.fromBoolean(result);
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
