package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

import java.util.List;

public class ArrayAccessExpression implements Expression {

    private final String name;
    private final List<Expression> indexes;

    public ArrayAccessExpression(String name, List<Expression> indexes) {
        this.name = name;
        this.indexes = indexes;
    }

    @Override
    public Value eval() {
        return getArray().get(lastIndex());
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

    private ArrayValue consumeArray(Value value){
        if(value instanceof ArrayValue arr){
            return arr;
        }
        throw new RuntimeException("Not array variable");
    }

    @Override
    public String toString() {
        return "ArrayAccessExpression{" +
                "n='" + name + '\'' +
                ", i=" + indexes +
                '}';
    }
}
