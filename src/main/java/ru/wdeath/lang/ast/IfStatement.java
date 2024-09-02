package ru.wdeath.lang.ast;

public class IfStatement implements Statement {

    public final Expression condition;
    public final Statement ifStatement, elseStatement;

    public IfStatement(Expression condition, Statement ifStatement, Statement elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        final var result = condition.eval().asInt();
        if (result != 0)
            ifStatement.execute();
        else if (elseStatement != null)
            elseStatement.execute();
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
        return "IF{" +
                "c=" + condition +
                ", if=" + ifStatement +
                ", else=" + elseStatement +
                '}';
    }
}
