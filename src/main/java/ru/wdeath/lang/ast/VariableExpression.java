package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.VariableDoesNotExistsException;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.lib.Value;

public class VariableExpression implements Node, Accessible {

    public final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        return get();
    }

    @Override
    public Value get() {
        if (!ScopeHandler.isVariableOrConstantExists(name)) throw new VariableDoesNotExistsException(name);
        return ScopeHandler.getVariableOrConstant(name);
    }

    @Override
    public Value set(Value value) {
        ScopeHandler.setVariable(name, value);
        return value;
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
        return name;
    }

}
