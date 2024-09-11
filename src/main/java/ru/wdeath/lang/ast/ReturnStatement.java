package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public class ReturnStatement extends RuntimeException implements Statement{

    public final Node expression;
    public Value result;

    public ReturnStatement(Node expression) {
        this.expression = expression;
    }

    public Value getResult() {
        return result;
    }

    @Override
    public Value eval() {
        result = expression.eval();
        throw this;
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
        return "return " + expression.toString();
    }
}
