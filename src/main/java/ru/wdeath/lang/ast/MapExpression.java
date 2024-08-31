package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.MapValue;
import ru.wdeath.lang.lib.Value;

import java.util.Map;

public class MapExpression implements Expression{

    public final Map<Expression, Expression> elements;

    public MapExpression(Map<Expression, Expression> arguments) {
        this.elements = arguments;
    }

    @Override
    public Value eval() {
        final int size = elements.size();
        final MapValue map = new MapValue(size);
        for (Expression key : elements.keySet()) {
            map.set(key.eval(), elements.get(key).eval());
        }
        return map;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
