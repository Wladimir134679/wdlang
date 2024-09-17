package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

public class IfStatement implements Statement {

    public final Node condition;
    public final Statement ifStatement, elseStatement;

    public IfStatement(Node condition, Statement ifStatement, Statement elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public Value eval() {
        final var result = condition.eval().asInt();
        if (result != 0)
            ifStatement.eval();
        else if (elseStatement != null)
            elseStatement.eval();
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
        return "IF " + condition;
    }
}
