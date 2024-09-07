package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.MapValue;
import ru.wdeath.lang.lib.Value;

import java.util.Map;

public class MapExpression implements Node{

    public final Map<Node, Node> elements;

    public MapExpression(Map<Node, Node> arguments) {
        this.elements = arguments;
    }

    @Override
    public Value eval() {
        final int size = elements.size();
        final MapValue map = new MapValue(size);
        for (Map.Entry<Node, Node> entry : elements.entrySet()) {
            map.set(entry.getKey().eval(), entry.getValue().eval());
        }
        return map;
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
        return elements.toString();
    }
}
