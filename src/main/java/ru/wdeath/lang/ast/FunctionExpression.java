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

        Function function = getFunction(name);
        if (function instanceof UserDefineFunction usersDef) {
            if (arguments.size() != usersDef.getArgsCount()) throw new RuntimeException("Args count mismatch");
            Variables.push();
            for (int i = 0; i < arguments.size(); i++) {
                Variables.set(usersDef.getArgsName(i), listValue[i]);
            }
            final var result = function.execute(listValue);
            Variables.pop();
            return result;
        }
        return function.execute(listValue);
    }

    private Function getFunction(String name) {
        if(Functions.isExists(name)) return Functions.getFunction(name);
        if(Variables.isExists(name)){
            final var value = Variables.get(name);
            if(value instanceof FunctionValue func)
                return func.getFunction();
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
