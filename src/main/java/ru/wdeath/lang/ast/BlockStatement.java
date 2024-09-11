package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement{

    public final List<Node> statements;

    public BlockStatement() {
        statements = new ArrayList<>();
    }

    public BlockStatement(List<Node> statements) {
        this.statements = statements;
    }

    public void addStatement(Node statement) {
        statements.add(statement);
    }

    @Override
    public Value eval() {
        statements.forEach(Node::eval);
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
        return "BlockStatement";
    }
}
