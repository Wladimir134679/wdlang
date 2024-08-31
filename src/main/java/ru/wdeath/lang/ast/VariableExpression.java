package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class VariableExpression implements Expression {

    public final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if(!Variables.isExists(name)) throw new RuntimeException("Variable does not exists: " + name);
        return Variables.get(name);
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
