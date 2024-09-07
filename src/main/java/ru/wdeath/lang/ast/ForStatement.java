package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

public class ForStatement implements Statement {

    public final Statement init;
    public final Node termination;
    public final Statement increment;
    public final Statement block;

    public ForStatement(Statement init, Node termination, Statement increment, Statement block) {
        this.init = init;
        this.termination = termination;
        this.increment = increment;
        this.block = block;
    }


    @Override
    public Value eval() {
        for (init.eval(); termination.eval().asInt() != 0; increment.eval()) {
            try {
                block.eval();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                //continue;
            }
        }
        return NumberValue.ZERO;
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
        return "FS{" +
                "init=" + init +
                ", t=" + termination +
                ", i=" + increment +
                ", b=" + block +
                '}';
    }
}
