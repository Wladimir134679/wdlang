package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.TypeException;
import com.wdeath.wdlang.script.lib.*;

import java.util.List;

public final class ContainerAccessExpression implements Expression, Accessible {

    public final ScriptProgram scriptProgram;
    public final List<Expression> indices;
    public final Expression root;
    private boolean rootIsVariable;

    public ContainerAccessExpression(String variable, List<Expression> indices, ScriptProgram scriptProgram) {
        this(scriptProgram, new VariableExpression(scriptProgram, variable), indices);
    }

    public ContainerAccessExpression(ScriptProgram scriptProgram, Expression root, List<Expression> indices) {
        this.scriptProgram = scriptProgram;
        rootIsVariable = root instanceof VariableExpression;
        this.root = root;
        this.indices = indices;
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
        switch (container.type()) {
            case Types.ARRAY:
                return ((ArrayValue) container).get(lastIndex);

            case Types.MAP:
                return ((MapValue) container).get(lastIndex);

            case Types.STRING:
                return ((StringValue) container).access(lastIndex);
                
            case Types.CLASS:
                return ((ClassInstanceValue) container).access(lastIndex);
                
            default:
                throw new TypeException("Array or map expected. Got " + Types.typeToString(container.type()));
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
                
            case Types.CLASS:
                ((ClassInstanceValue) container).set(lastIndex, value);
                return value;
                
            default:
                throw new TypeException("Array or map expected. Got " + container.type());
        }
    }
    
    public Value getContainer() {
        Value container = root.eval();
        final int last = indices.size() - 1;
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
    
    public Value lastIndex() {
        return index(indices.size() - 1);
    }
    
    private Value index(int index) {
        return indices.get(index).eval();
    }
    
    public MapValue consumeMap(Value value) {
        if (value.type() != Types.MAP) {
            throw new TypeException("Map expected");
        }
        return (MapValue) value;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return root.toString() + indices;
    }
}
