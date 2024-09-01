package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.*;

import java.util.List;

public class ArrayAccessExpression implements Expression {

    public final String name;
    public final List<Expression> indexes;

    public ArrayAccessExpression(String name, List<Expression> indexes) {
        this.name = name;
        this.indexes = indexes;
    }

    @Override
    public Value eval() {
        Value container = Variables.get(name);
        if (container.type() == Types.ARRAY) {
            return getArray().get(lastIndex());
        }
        return consumeMap(container).get(indexes.getFirst().eval());
    }

    public ArrayValue getArray(){
        var variable = consumeArray(Variables.get(name));
        final int last = indexes.size() - 1;
        for (int i = 0; i < last; i++) {
            variable = consumeArray(variable.get(index(i)));
        }
        return variable;
    }

    public int lastIndex(){
        return index(indexes.size() - 1);
    }

    private int index(int i) {
        return (int) indexes.get(i).eval().asDouble();
    }

    public MapValue consumeMap(Value value) {
        if (value.type() == Types.MAP) {
            return (MapValue) value;
        } else {
            throw new TypeException("Map expected");
        }
    }

    private ArrayValue consumeArray(Value value){
        if(value.type() == Types.ARRAY){
            return (ArrayValue) value;
        }
        throw new TypeException("Not array variable");
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ArrayAccessExpression{" +
                "n='" + name + '\'' +
                ", i=" + indexes +
                '}';
    }
}
