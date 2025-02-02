package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ast.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class LoopStatementsValidator  extends LintVisitor {
    private enum LoopScope { FOR, WHILE, DO_WHILE, FOREACH_ARR, FOREACH_MAP };

    private final Deque<LoopScope> loopScope;

    LoopStatementsValidator(LinterResults results) {
        super(results);
        loopScope = new ArrayDeque<>(10);
    }

    @Override
    public void visit(ForStatement s) {
        s.init.accept(this);
        s.termination.accept(this);
        s.increment.accept(this);
        loopScope.push(LoopScope.FOR);
        s.block.accept(this);
        loopScope.remove();
    }

    @Override
    public void visit(DoWhileStatement s) {
        s.condition.accept(this);
        loopScope.push(LoopScope.DO_WHILE);
        s.body.accept(this);
        loopScope.remove();
    }

    @Override
    public void visit(ForeachArrayStatement s) {
        s.container.accept(this);
        loopScope.push(LoopScope.FOREACH_ARR);
        s.body.accept(this);
        loopScope.remove();
    }

    @Override
    public void visit(ForeachMapStatement s) {
        s.container.accept(this);
        loopScope.push(LoopScope.FOREACH_MAP);
        s.body.accept(this);
        loopScope.remove();
    }

    @Override
    public void visit(WhileStatement s) {
        s.condition.accept(this);
        loopScope.push(LoopScope.WHILE);
        s.body.accept(this);
        loopScope.remove();
    }

    @Override
    public void visit(BreakStatement s) {
        if (loopScope.isEmpty()) {
            results.add(LinterResult.error(
                    "break statement shouldn't be placed outside the loop body",
                    s.getRange()));
        }
    }

    @Override
    public void visit(ContinueStatement s) {
        if (loopScope.isEmpty()) {
            results.add(LinterResult.error(
                    "continue statement shouldn't be placed outside the loop body",
                    s.getRange()));
        }
    }
}
