package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class AssignmentExpression implements Expression{

    public final Accessible target;
    public final BinaryExpression.Operator operation;
    public final Expression expression;

    public AssignmentExpression(BinaryExpression.Operator operation, Accessible target, Expression expr) {
        this.operation = operation;
        this.target = target;
        this.expression = expr;
    }

    @Override
    public Value eval() {
        if (operation == null) {
            // Simple assignment
            return target.set(expression.eval());
        }
        final Expression expr1 = new ValueExpression(target.get());
        final Expression expr2 = new ValueExpression(expression.eval());
        return target.set(new BinaryExpression(operation, expr1, expr2).eval());
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "AssignmentExpression{" +
                "t=" + target +
                ", o=" + operation +
                ", e=" + expression +
                '}';
    }
}
