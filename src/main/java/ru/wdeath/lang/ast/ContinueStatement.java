package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

public class ContinueStatement extends RuntimeException implements Statement, SourceLocation {

    private final Range range;

    public ContinueStatement(Range range) {
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public Value eval() {
        throw this;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public String toString() {
        return "CS{}";
    }
}
