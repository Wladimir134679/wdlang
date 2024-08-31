package ru.wdeath.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement{

    private final List<Statement> statements;

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
    public String toString() {
        return "BS{" +
                "s=" + statements +
                '}';
    }
}
