package ru.wdeath.lang.ast;

public class FunctionStatement implements Statement{

    private final Expression function;

    public FunctionStatement(Expression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.eval();
    }

    @Override
    public String toString() {
        return "FuncS{" +
                "f=" + function +
                '}';
    }
}
