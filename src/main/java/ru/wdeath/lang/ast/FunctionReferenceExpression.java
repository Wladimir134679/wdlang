package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.FunctionValue;
import ru.wdeath.lang.lib.Functions;

public class FunctionReferenceExpression implements Expression {

    public final String name;

    public FunctionReferenceExpression(String name) {
        this.name = name;
    }

    @Override
    public FunctionValue eval() {
        return new FunctionValue(Functions.getFunction(name));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "FunctionReferenceExpression{" +
                "n='" + name + '\'' +
                '}';
    }
}
