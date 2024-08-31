package ru.wdeath.lang.ast;

public class ForStatement implements Statement {

    private final Statement init;
    private final Expression termination;
    private final Statement increment;
    private final Statement block;

    public ForStatement(Statement init, Expression termination, Statement increment, Statement block) {
        this.init = init;
        this.termination = termination;
        this.increment = increment;
        this.block = block;
    }


    @Override
    public void execute() {
        for (init.execute(); termination.eval().asDouble() != 0; increment.execute()) {
            try {
                block.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                //continue;
            }
        }
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
