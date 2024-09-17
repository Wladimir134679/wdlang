package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.lib.UserDefinedFunction;
import ru.wdeath.lang.lib.classes.ClassDeclaration;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.classes.ClassField;
import ru.wdeath.lang.lib.classes.ClassMethod;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclarationStatement implements Statement, SourceLocation {

    public ProgramContext programContext;
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
        final var classFields = fields.stream()
                .map(this::toClassField)
                .toList();
        final var classMethods = methods.stream()
                .map(this::toClassMethod)
                .toList();
        final var declaration = new ClassDeclaration(name, classFields, classMethods);
        programContext.getScope().setClassDeclaration(declaration);
        return NumberValue.ZERO;
    }
    private ClassField toClassField(AssignmentExpression f) {
        // TODO check only variable assignments
        final String fieldName = ((VariableExpression) f.target).name;
        return new ClassField(fieldName, f);
    }

    private ClassMethod toClassMethod(FunctionDefineStatement m) {
        final var function = new UserDefinedFunction(m.arguments, m.body, m.getRange());
        return new ClassMethod(m.name, function);
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
        return String.format("class %s", name);
    }
}
