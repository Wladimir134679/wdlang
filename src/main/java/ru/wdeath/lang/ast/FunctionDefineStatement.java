package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

public class FunctionDefineStatement implements Statement, SourceLocation {

    public final String name;
    public final Arguments arguments;
    public final Statement body;
    private final Range range;

    public FunctionDefineStatement(String name, Arguments arguments, Statement body, Range range) {
        this.name = name;
        this.arguments = arguments;
        this.body = body;
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public Value eval() {
        ScopeHandler.setFunction(name, new UserDefinedFunction(arguments, body, range));
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
        return "FuncDefS{" +
                "n='" + name + '\'' +
                ", a=" + arguments +
                ", b=" + body +
                '}';
    }
}
