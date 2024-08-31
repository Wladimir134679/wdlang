package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class VariableExpression implements Expression {

    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public Value eval() {
        if(!Variables.isExists(name)) throw new RuntimeException("Variable does not exists");
        return Variables.get(name);
    }

    @Override
    public String toString() {
        return "VE{n='" + name + "'}";
    }
}
