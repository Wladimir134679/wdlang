package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionExpression implements Expression {

    public final String name;
    public final List<Expression> arguments;

    public FunctionExpression(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
    }

    public FunctionExpression(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public void addArgument(Expression argument) {
        arguments.add(argument);
    }

    @Override
    public Value eval() {
        final var listValue = new Value[arguments.size()];
        for (int i = 0; i < arguments.size(); i++) {
            listValue[i] = arguments.get(i).eval();
        }

        return getFunction(name).execute(listValue);
    }

    private Function getFunction(String name) {
        if (Functions.isExists(name)) return Functions.getFunction(name);
        if (Variables.isExists(name)) {
            final var value = Variables.get(name);
            if (value.type() == Types.FUNCTION)
                return ((FunctionValue) value).getFunction();
        }
        throw new RuntimeException("Function '" + name + "' not found");
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "FuncE{" +
                "n='" + name + '\'' +
                ", args=" + arguments +
                '}';
    }
}
