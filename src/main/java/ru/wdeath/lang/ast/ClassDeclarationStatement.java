package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.ClassDeclarations;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclarationStatement implements Statement, SourceLocation {

    public final String name;
    public final List<FunctionDefineStatement> methods;
    public final List<AssignmentExpression> fields;
    private final Range range;

    public ClassDeclarationStatement(String name) {
        this(name, Range.ZERO);
    }

    public ClassDeclarationStatement(String name, Range range) {
        this.name = name;
        this.range = range;
        methods = new ArrayList<>();
        fields = new ArrayList<>();
    }

    @Override
    public Range getRange() {
        return range;
    }

    public void addField(AssignmentExpression expr) {
        fields.add(expr);
    }

    public void addMethod(FunctionDefineStatement statement) {
        methods.add(statement);
    }

    @Override
    public Value eval() {
        ClassDeclarations.set(name, this);
        return NumberValue.ZERO;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return String.format("class %s {\n  %s  %s}", name, fields, methods);
    }
}
