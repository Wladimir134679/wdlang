package ru.wdeath.lang.ast;

public class DoWhileStatement implements Statement {

    private final Expression condition;
    private final Statement body;

    public DoWhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public void execute() {
        do {
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                //continue;
            }
        } while (condition.eval().asDouble() != 0);
    }

    @Override
    public String toString() {
        return "DWS{" +
                "c=" + condition +
                ", b=" + body +
                '}';
    }
}
