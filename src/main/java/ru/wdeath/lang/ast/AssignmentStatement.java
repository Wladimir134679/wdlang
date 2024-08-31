package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Variables;

public class AssignmentStatement implements Statement{

    public final String name;
    public final Expression expression;

    public AssignmentStatement(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public void execute() {
        Variables.set(name, expression.eval());
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "AS{" +
                "n='" + name + '\'' +
                ", e=" + expression +
                '}';
    }
}
