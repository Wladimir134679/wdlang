package ru.wdeath.lang.ast;

public class ExprStatement implements Statement{

    public final Expression function;

    public ExprStatement(Expression function) {
        this.function = function;
    }

    @Override
    public void execute() {
        function.eval();
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
        return "FuncS{" +
                "f=" + function +
                '}';
    }
}
