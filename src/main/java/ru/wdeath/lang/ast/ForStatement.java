package ru.wdeath.lang.ast;

public class ForStatement implements Statement {

    public final Statement init;
    public final Expression termination;
    public final Statement increment;
    public final Statement block;

    public ForStatement(Statement init, Expression termination, Statement increment, Statement block) {
        this.init = init;
        this.termination = termination;
        this.increment = increment;
        this.block = block;
    }


    @Override
    public void execute() {
        for (init.execute(); termination.eval().asInt() != 0; increment.execute()) {
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
    public void accept(Visitor visitor) {
        visitor.visit(this);
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
