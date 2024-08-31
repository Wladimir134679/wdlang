package ru.wdeath.lang.ast;

public class IfStatement implements Statement {

    private final Expression condition;
    private final Statement ifStatement, elseStatement;

    public IfStatement(Expression condition, Statement ifStatement, Statement elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        final double result = condition.eval().asDouble();
        if (result != 0)
            ifStatement.execute();
        else if (elseStatement != null)
            elseStatement.execute();
    }

    @Override
    public String toString() {
        return "IF{" +
                "c=" + condition +
                ", if=" + ifStatement +
                ", else=" + elseStatement +
                '}';
    }
}
