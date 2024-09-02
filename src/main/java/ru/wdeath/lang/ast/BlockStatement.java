package ru.wdeath.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement{

    public final List<Statement> statements;

    public BlockStatement() {
        statements = new ArrayList<>();
    }

    public BlockStatement(List<Statement> statements) {
        this.statements = statements;
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        statements.forEach(Statement::execute);
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
        return "BS{" +
                "s=" + statements +
                '}';
    }
}
