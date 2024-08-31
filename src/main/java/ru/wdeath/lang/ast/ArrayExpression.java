package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.Value;

import java.util.List;

public class ArrayExpression implements Expression {

    private final List<Expression> arguments;

    public ArrayExpression(List<Expression> arguments) {
        this.arguments = arguments;
    }

    @Override
    public Value eval() {
        final var size = arguments.size();
        final ArrayValue arr = new ArrayValue(size);
        for (int i = 0; i < arguments.size(); i++) {
            arr.set(i, arguments.get(i).eval());
        }
        return arr;
    }

    @Override
    public String toString() {
        return "ArrayExpression{" +
                "a=" + arguments +
                '}';
    }
}
