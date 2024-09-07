package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.*;

import java.util.List;
import java.util.regex.Pattern;

public class ContainerAccessExpression implements Expression, Accessible {

    private static final Pattern PATTERN_SIMPLE_INDEX = Pattern.compile("^\"[a-zA-Z$_]\\w*\"");

    public final Expression root;
    public final List<Expression> indexes;
    private boolean rootIsVariable;

    public ContainerAccessExpression(String variable, List<Expression> indexes) {
        this(new VariableExpression(variable), indexes);
    }

    public ContainerAccessExpression(Expression root, List<Expression> indices) {
        rootIsVariable = root instanceof VariableExpression;
        this.root = root;
        this.indexes = indices;
    }

    public boolean rootIsVariable() {
        return rootIsVariable;
    }

    public Expression getRoot() {
        return root;
    }

    @Override
    public Value eval() {
        return get();
    }

    @Override
    public Value get() {
        final Value container = getContainer();
        final Value lastIndex = lastIndex();
        return switch (container.type()) {
            case Types.ARRAY -> {
                final int arrayIndex = lastIndex.asInt();
                yield ((ArrayValue) container).get(arrayIndex);
            }
            case Types.MAP -> ((MapValue) container).get(lastIndex);
            case Types.STRING -> ((StringValue) container).access(lastIndex);
            case Types.CLASS -> ((ClassInstanceValue) container).access(lastIndex);
            default -> throw new TypeException("Array or map expected");
        };
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
        Value container = root.eval();
        final int last = indexes.size() - 1;
        for (int i = 0; i < last; i++) {
            final Value index = index(i);
            container = switch (container.type()) {
                case Types.ARRAY -> {
                    final int arrayIndex = index.asInt();
                    yield ((ArrayValue) container).get(arrayIndex);
                }
                case Types.MAP -> ((MapValue) container).get(index);
                default -> throw new TypeException("Array or map expected");
            };
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
        final var sb = new StringBuilder(root.toString());
        int i = 0;
        for (Node index : indexes) {
            String indexStr = index.toString();
            if (precomputeSimpleIndices()[i]) {
                sb.append('.').append(indexStr, 1, indexStr.length() - 1);
            } else {
                sb.append('[').append(indexStr).append(']');
            }
            i++;
        }
        return sb.toString();
    }

    private boolean[] precomputeSimpleIndices() {
        final boolean[] result = new boolean[indexes.size()];
        int i = 0;
        for (Node index : indexes) {
            String indexStr = index.toString();
            result[i] = PATTERN_SIMPLE_INDEX.matcher(indexStr).matches();
            i++;
        }
        return result;
    }
}
