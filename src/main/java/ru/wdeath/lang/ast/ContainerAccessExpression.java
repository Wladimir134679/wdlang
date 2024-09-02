package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.*;

import java.util.List;

public class ContainerAccessExpression implements Expression, Accessible {

    public final String name;
    public final List<Expression> indexes;

    public ContainerAccessExpression(String name, List<Expression> indexes) {
        this.name = name;
        this.indexes = indexes;
    }

    @Override
    public Value eval() {
        return get();
    }

    @Override
    public Value get() {
        final Value container = getContainer();
        final Value lastIndex = lastIndex();
        switch (container.type()) {
            case Types.ARRAY:
                final int arrayIndex = lastIndex.asInt();
                return ((ArrayValue) container).get(arrayIndex);

            case Types.MAP:
                return ((MapValue) container).get(lastIndex);

            default:
                throw new TypeException("Array or map expected");
        }
    }

    @Override
    public Value set(Value value) {
        final Value container = getContainer();
        final Value lastIndex = lastIndex();
        switch (container.type()) {
            case Types.ARRAY:
                final int arrayIndex = lastIndex.asInt();
                ((ArrayValue) container).set(arrayIndex, value);
                return value;

            case Types.MAP:
                ((MapValue) container).set(lastIndex, value);
                return value;

            default:
                throw new TypeException("Array or map expected. Got " + container.type());
        }
    }

    public Value getContainer() {
        Value container = Variables.get(name);
        final int last = indexes.size() - 1;
        for (int i = 0; i < last; i++) {
            final Value index = index(i);
            switch (container.type()) {
                case Types.ARRAY:
                    final int arrayIndex = index.asInt();
                    container = ((ArrayValue) container).get(arrayIndex);
                    break;

                case Types.MAP:
                    container = ((MapValue) container).get(index);
                    break;

                default:
                    throw new TypeException("Array or map expected");
            }
        }
        return container;
    }

    public Value lastIndex(){
        return index(indexes.size() - 1);
    }

    private Value index(int i) {
        return indexes.get(i).eval();
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
        return "ArrayAccessExpression{" +
                "n='" + name + '\'' +
                ", i=" + indexes +
                '}';
    }
}
