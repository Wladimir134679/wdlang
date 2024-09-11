package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public record Argument(String name, Node valueExpr) implements Node{

    public Argument(String name) {
        this(name, null);
    }

    @Override
    public Value eval() {
        return null;
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
        return name + (valueExpr == null ? "" : " = " + valueExpr);
    }

}
