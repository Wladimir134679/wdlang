package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public class ContinueStatement extends RuntimeException implements Statement{

    @Override
    public Value eval() {
        throw this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public String toString() {
        return "CS{}";
    }
}
