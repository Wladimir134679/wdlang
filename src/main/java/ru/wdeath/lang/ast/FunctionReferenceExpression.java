package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.Function;
import ru.wdeath.lang.lib.FunctionValue;
import ru.wdeath.lang.lib.Types;
import ru.wdeath.lang.lib.Value;

public class FunctionReferenceExpression implements Node {

    public final ProgramContext programContext;
    public final String name;

    public FunctionReferenceExpression(ProgramContext programContext, String name) {
        this.programContext = programContext;
        this.name = name;
    }

    @Override
    public FunctionValue eval() {
        return new FunctionValue(programContext.getScope().getFunction(name));
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
        return "FunctionReferenceExpression " + name;
    }
}
