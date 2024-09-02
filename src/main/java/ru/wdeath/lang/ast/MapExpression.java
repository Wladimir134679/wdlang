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
        for (Map.Entry<Expression, Expression> entry : elements.entrySet()) {
            map.set(entry.getKey().eval(), entry.getValue().eval());
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
