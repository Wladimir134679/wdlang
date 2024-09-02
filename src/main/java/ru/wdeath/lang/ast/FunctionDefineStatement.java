package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.UserDefinedFunction;

public class FunctionDefineStatement implements Statement{

    public final String name;
    public final Arguments arguments;
    public final Statement body;

    public FunctionDefineStatement(String name, Arguments arguments, Statement body) {
        this.name = name;
        this.arguments = arguments;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.addFunction(name, new UserDefinedFunction(arguments, body));
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
