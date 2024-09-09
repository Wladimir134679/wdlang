package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.classes.EvaluableValue;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

public class AssignmentExpression implements Statement, SourceLocation, EvaluableValue {

    public final Accessible target;
    public final BinaryExpression.Operator operation;
    public final Node expression;
    public final Range range;

    public AssignmentExpression(BinaryExpression.Operator operation, Accessible target, Node expr, Range range) {
        this.operation = operation;
        this.target = target;
        this.expression = expr;
        this.range = range;
    }

    @Override
    public Value eval() {
        if (operation == null) {
            // Simple assignment
            return target.set(expression.eval());
        }
        final Node expr1 = new ValueExpression(target.get());
        final Node expr2 = new ValueExpression(expression.eval());
        return target.set(new BinaryExpression(operation, expr1, expr2).eval());
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
        final String op = (operation == null) ? "" : operation.toString();
        return String.format("%s %s= %s", target, op, expression);
    }
}
