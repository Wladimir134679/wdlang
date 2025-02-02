package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public class ExprStatement implements Node, Statement{

    public final Node expr;

    public ExprStatement(Node expr) {
        this.expr = expr;
    }

    @Override
    public Value eval() {
        return expr.eval();
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
        return "ExprStatement " + expr;
    }
}
