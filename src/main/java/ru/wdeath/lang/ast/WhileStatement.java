package ru.wdeath.lang.ast;

public class WhileStatement implements Statement {

    private final Expression condition;
    private final Statement body;

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
    public String toString() {
        return "WS{" +
                "c=" + condition +
                ", b=" + body +
                '}';
    }
}
