package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.UnknownFunctionException;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FunctionExpression implements Expression, Statement, SourceLocation {

    public final Expression expression;
    public final List<Expression> arguments;
    private Range range;

    public FunctionExpression(Expression expression) {
        this.expression = expression;
        this.arguments = new ArrayList<>();
    }

    public FunctionExpression(Expression expression, List<Expression> arguments) {
        this.expression = expression;
        this.arguments = arguments;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }


    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        final int size = arguments.size();
        final Value[] values = new Value[size];
        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }
        final Function f = consumeFunction(expression);
        CallStack.enter(expression.toString(), f, range);
        final Value result = f.execute(values);
        CallStack.exit();
        return result;
    }

    private Function getFunction(String name) {
        if (ScopeHandler.isFunctionExists(name)) {
            return ScopeHandler.getFunction(name);
        }
        if (ScopeHandler.isVariableOrConstantExists(name)) {
            final Value value = ScopeHandler.getVariableOrConstant(name);
            if (value.type() == Types.FUNCTION)
                return ((FunctionValue) value).getFunction();
        }
        throw new UnknownFunctionException(name);
    }

    private Function consumeFunction(Expression expr) {
        final Value value = expr.eval();
        if (value.type() == Types.FUNCTION) {
            return ((FunctionValue) value).getFunction();
        }
        return getFunction(value.asString());
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
        final StringBuilder sb = new StringBuilder();
        if (expression instanceof ValueExpression valueExpr && (valueExpr.value.type() == Types.STRING)) {
            sb.append(valueExpr.value.asString()).append('(');
        } else {
            sb.append(expression).append('(');
        }
        final Iterator<Expression> it = arguments.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(", ").append(it.next());
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
