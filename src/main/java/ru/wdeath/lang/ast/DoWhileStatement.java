package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;

public class DoWhileStatement implements Statement {

    public final Node condition;
    public final Statement body;

    public DoWhileStatement(Node condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Value eval() {
        do {
            try {
                body.eval();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                //continue;
            }
        } while (condition.eval().asInt() != 0);
        return NumberValue.ZERO;
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
        return "DWS{" +
                "c=" + condition +
                ", b=" + body +
                '}';
    }
}
