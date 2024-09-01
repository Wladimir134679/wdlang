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
        double n1, n2;
        Value eval1 = expr1.eval();
        Value eval2 = expr2.eval();
        if (eval1.type() == Types.STRING) {
            n1 = eval1.asString().compareTo(eval2.asString());
            n2 = 0;
        } else {
            n1 = eval1.asDouble();
            n2 = eval2.asDouble();
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
            default -> throw new OperationIsNotSupportedException(operation);
        };
        return NumberValue.fromBoolean(result);
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
