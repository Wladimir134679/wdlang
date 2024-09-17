package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public class TernaryExpression implements Node{

    public final Node condition;
    public final Node trueExpr, falseExpr;

    public TernaryExpression(Node condition, Node trueExpr, Node falseExpr) {
        this.condition = condition;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public Value eval() {
        if(condition.eval().asInt() != 0){
            return trueExpr.eval();
        }else {
            return falseExpr.eval();
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return condition.toString() + " ? " + trueExpr.toString() + " : " + falseExpr.toString();
    }
}
