package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.VariableDoesNotExistsException;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class VariableExpression implements Expression, Accessible {

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
        if(!Variables.isExists(name)) throw new VariableDoesNotExistsException(name);
        return Variables.get(name);
    }

    @Override
    public Value set(Value value) {
        Variables.set(name, value);
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "VE{n='" + name + "'}";
    }

}
