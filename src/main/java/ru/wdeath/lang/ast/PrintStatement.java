package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

public class PrintStatement implements Statement{

    public ProgramContext programContext;
    public final Node expression;

    public PrintStatement(Node expression) {
        this.expression = expression;
    }

    @Override
    public Value eval() {
        programContext.getConsole().print(expression.eval().asString());
        return NumberValue.ZERO;
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
        return "print " + expression;
    }
}
