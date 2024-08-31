package ru.wdeath.lang.ast;

public class WhileStatement implements Statement {

    public final Expression condition;
    public final Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        while (condition.eval().asDouble() != 0) {
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                //continue;
            }
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "WS{" +
                "c=" + condition +
                ", b=" + body +
                '}';
    }
}
