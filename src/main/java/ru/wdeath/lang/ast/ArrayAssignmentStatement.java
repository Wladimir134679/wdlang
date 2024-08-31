package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ArrayValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class ArrayAssignmentStatement implements Statement {

    private final ArrayAccessExpression array;
    private final Expression expression;

    public ArrayAssignmentStatement(ArrayAccessExpression array, Expression expression) {
        this.array = array;
        this.expression = expression;
    }


    @Override
    public void execute() {
        array.getArray().set(array.lastIndex(), expression.eval());
    }

    @Override
    public String toString() {
        return "ArrayAssignmentStatement{" +
                "a='" + array + '\'' +
                ", e=" + expression +
                '}';
    }
}
