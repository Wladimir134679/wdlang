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
        switch (operation) {
            case ADD:
                return add(value1, value2);
            case SUBTRACT:
                return subtract(value1, value2);
            case MULTIPLY:
                return multiply(value1, value2);
            case DIVIDE:
                return divide(value1, value2);
            case REMAINDER:
                return remainder(value1, value2);
            case PUSH:
                return push(value1, value2);
            case AND:
                return and(value1, value2);
            case OR:
                return or(value1, value2);
            case XOR:
                return xor(value1, value2);
            case LSHIFT:
                return lshift(value1, value2);
            case RSHIFT:
                return rshift(value1, value2);
            case URSHIFT:
                return urshift(value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation);
        }
    }

    private Value add(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return add((NumberValue) value1, value2);
            case Types.ARRAY:
                return ArrayValue.add((ArrayValue) value1, value2);
            case Types.MAP:
                if (value2.type() != Types.MAP)
                    throw new TypeException("Cannot merge non map value to map");
                return MapValue.merge((MapValue) value1, (MapValue) value2);
            case Types.FUNCTION: /* TODO: combining functions */
            case Types.STRING:
            default:
                // Concatenation strings
                return new StringValue(value1.asString() + value2.asString());
        }
    }

    private Value add(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 + number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() + number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() + number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() + number2.longValue());
            }
            return NumberValue.of(number1.intValue() + number2.intValue());
        }
        // number1 + other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() + value2.asDouble());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() + value2.asDouble());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() + value2.asInt());
        }
        return NumberValue.of(number1.intValue() + value2.asInt());
    }

    private Value subtract(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return subtract((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value subtract(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 - number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() - number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() - number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() - number2.longValue());
            }
            return NumberValue.of(number1.intValue() - number2.intValue());
        }
        // number1 - other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() - value2.asDouble());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() - value2.asDouble());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() - value2.asInt());
        }
        return NumberValue.of(number1.intValue() - value2.asInt());
    }


    private Value multiply(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return multiply((NumberValue) value1, value2);
            case Types.STRING: {
                final String string1 = value1.asString();
                final int iterations = value2.asInt();
                final StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < iterations; i++) {
                    buffer.append(string1);
                }
                return new StringValue(buffer.toString());
            }
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value multiply(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 * number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() * number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() * number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() * number2.longValue());
            }
            return NumberValue.of(number1.intValue() * number2.intValue());
        }
        // number1 * other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() * value2.asDouble());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() * value2.asDouble());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() * value2.asInt());
        }
        return NumberValue.of(number1.intValue() * value2.asInt());
    }

    private Value divide(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return divide((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value divide(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 / number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() / number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() / number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() / number2.longValue());
            }
            return NumberValue.of(number1.intValue() / number2.intValue());
        }
        // number1 / other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() / value2.asDouble());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() / value2.asDouble());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() / value2.asInt());
        }
        return NumberValue.of(number1.intValue() / value2.asInt());
    }

    private Value remainder(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return remainder((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value remainder(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 % number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Double || number2 instanceof Double) {
                return NumberValue.of(number1.doubleValue() % number2.doubleValue());
            }
            if (number1 instanceof Float || number2 instanceof Float) {
                return NumberValue.of(number1.floatValue() % number2.floatValue());
            }
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() % number2.longValue());
            }
            return NumberValue.of(number1.intValue() % number2.intValue());
        }
        // number1 % other
        if (number1 instanceof Double) {
            return NumberValue.of(number1.doubleValue() % value2.asDouble());
        }
        if (number1 instanceof Float) {
            return NumberValue.of(number1.floatValue() % value2.asDouble());
        }
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() % value2.asInt());
        }
        return NumberValue.of(number1.intValue() % value2.asInt());
    }

    private Value push(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.ARRAY:
                return ArrayValue.add((ArrayValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value and(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return and((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value and(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 & number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() & number2.longValue());
            }
            return NumberValue.of(number1.intValue() & number2.intValue());
        }
        // number1 & other
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() & value2.asInt());
        }
        return NumberValue.of(number1.intValue() & value2.asInt());
    }

    private Value or(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return or((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value or(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 | number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() | number2.longValue());
            }
            return NumberValue.of(number1.intValue() | number2.intValue());
        }
        // number1 | other
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() | value2.asInt());
        }
        return NumberValue.of(number1.intValue() | value2.asInt());
    }

    private Value xor(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return xor((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value xor(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 ^ number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() ^ number2.longValue());
            }
            return NumberValue.of(number1.intValue() ^ number2.intValue());
        }
        // number1 ^ other
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() ^ value2.asInt());
        }
        return NumberValue.of(number1.intValue() ^ value2.asInt());
    }

    private Value lshift(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return lshift((NumberValue) value1, value2);
            case Types.ARRAY: {
                if (value2.type() != Types.ARRAY)
                    throw new TypeException("Cannot merge non array value to array");
                return ArrayValue.merge((ArrayValue) value1, (ArrayValue) value2);
            }
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value lshift(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 << number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() << number2.longValue());
            }
            return NumberValue.of(number1.intValue() << number2.intValue());
        }
        // number1 << other
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() << value2.asInt());
        }
        return NumberValue.of(number1.intValue() << value2.asInt());
    }

    private Value rshift(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return rshift((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value rshift(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 >> number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() >> number2.longValue());
            }
            return NumberValue.of(number1.intValue() >> number2.intValue());
        }
        // number1 >> other
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() >> value2.asInt());
        }
        return NumberValue.of(number1.intValue() >> value2.asInt());
    }

    private Value urshift(Value value1, Value value2) {
        switch (value1.type()) {
            case Types.NUMBER:
                return urshift((NumberValue) value1, value2);
            default:
                throw new OperationIsNotSupportedException(operation, "for " + Types.typeToString(value1.type()));
        }
    }

    private Value urshift(NumberValue value1, Value value2) {
        final Number number1 = value1.raw();
        if (value2.type() == Types.NUMBER) {
            // number1 >>> number2
            final Number number2 = ((NumberValue) value2).raw();
            if (number1 instanceof Long || number2 instanceof Long) {
                return NumberValue.of(number1.longValue() >>> number2.longValue());
            }
            return NumberValue.of(number1.intValue() >>> number2.intValue());
        }
        // number1 >>> other
        if (number1 instanceof Long) {
            return NumberValue.of(number1.longValue() >>> value2.asInt());
        }
        return NumberValue.of(number1.intValue() >>> value2.asInt());
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
                ", o=" + operation.name +
                ", e2=" + expr2 +
                '}';
    }
}
