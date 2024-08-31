package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class ArrayAssignmentStatement implements Statement {

    public final ArrayAccessExpression array;
    public final Expression expression;

    public ArrayAssignmentStatement(ArrayAccessExpression array, Expression expression) {
        this.array = array;
        this.expression = expression;
    }


    @Override
    public void execute() {
        final Value container = Variables.get(array.name);
        if (container instanceof ArrayValue) {
            array.getArray().set(array.lastIndex(), expression.eval());
            return;
        }
        array.consumeMap(container).set(array.indexes.getFirst().eval(), expression.eval());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ArrayAssignmentStatement{" +
                "a='" + array + '\'' +
                ", e=" + expression +
                '}';
    }
}
