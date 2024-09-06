package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.ArgumentsMismatchException;
import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.exception.UnknownFunctionException;
import ru.wdeath.lang.exception.VariableDoesNotExistsException;
import ru.wdeath.lang.lib.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression implements Expression, Statement {

    public final Expression expression;
    public final List<Expression> arguments;

    public FunctionExpression(Expression expression) {
        this.expression = expression;
        this.arguments = new ArrayList<>();
    }

    public FunctionExpression(Expression expression, List<Expression> arguments) {
        this.expression = expression;
        this.arguments = arguments;
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
        CallStack.enter(expression.toString(), f);
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
        return "FuncE{" +
                "e='" + expression + '\'' +
                ", args=" + arguments +
                '}';
    }
}
