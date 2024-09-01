package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.MapValue;
import ru.wdeath.lang.lib.Types;
import ru.wdeath.lang.lib.Value;

public class ContainerAssignmentStatement implements Statement {

    public final ContainerAccessExpression containerExpr;
    public final Expression expression;

    public ContainerAssignmentStatement(ContainerAccessExpression containerExpr, Expression expression) {
        this.containerExpr = containerExpr;
        this.expression = expression;
    }


    @Override
    public void execute() {
        final Value container = containerExpr.getContainer();
        final Value lastIndex = containerExpr.lastIndex();
        switch (container.type()) {
            case Types.ARRAY:
                final int arrayIndex = (int) lastIndex.asDouble();
                ((ArrayValue) container).set(arrayIndex, expression.eval());
                return;

            case Types.MAP:
                ((MapValue) container).set(lastIndex, expression.eval());
                return;

            default:
                throw new TypeException("Array or map expected. Got " + container.type());
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ArrayAssignmentStatement{" +
                "a='" + containerExpr + '\'' +
                ", e=" + expression +
                '}';
    }
}
