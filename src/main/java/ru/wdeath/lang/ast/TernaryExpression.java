package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public class TernaryExpression implements Expression{

    public final Expression condition;
    public final Expression trueExpr, falseExpr;

    public TernaryExpression(Expression condition, Expression trueExpr, Expression falseExpr) {
        this.condition = condition;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
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
}
