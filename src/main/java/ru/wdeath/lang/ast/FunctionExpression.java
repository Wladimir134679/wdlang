package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.UnknownFunctionException;
import ru.wdeath.lang.exception.VariableDoesNotExistsException;
import ru.wdeath.lang.lib.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression implements Expression, Statement{

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
        final var listValue = new Value[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            listValue[i] = arguments.get(i).eval();
        }

        final Function f = consumeFunction(expression);
        CallStack.enter(expression.toString(), f);
        final Value result = f.execute(listValue);
        CallStack.exit();
        return result;
    }

    private Function getFunction(String name) {
        if (Functions.isExists(name)) return Functions.getFunction(name);
        if (Variables.isExists(name)) {
            final var value = Variables.get(name);
            if (value.type() == Types.FUNCTION)
                return ((FunctionValue) value).getFunction();
        }
        throw new UnknownFunctionException(name);
    }

    private Function consumeFunction(Expression expr) {
        try {
            final Value value = expr.eval();
            if (value.type() == Types.FUNCTION) {
                return ((FunctionValue) value).getFunction();
            }
            return getFunction(value.asString());
        } catch (VariableDoesNotExistsException ex) {
            return getFunction(ex.getVariable());
        }
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
