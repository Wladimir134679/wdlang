package ru.wdeath.lang.ast;

public class BreakStatement extends RuntimeException implements Statement{

    @Override
    public void execute() {
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
        return "BS{}";
    }
}
