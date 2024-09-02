package ru.wdeath.lang.visitors;

import ru.wdeath.lang.ast.*;
import ru.wdeath.lang.parser.Optimizer;

public class DeadCodeElimination  extends OptimizationVisitor<Void> implements Optimizer.Info {

    private int ifStatementEliminatedCount;
    private int ternaryExpressionEliminatedCount;
    private int whileStatementEliminatedCount;

    @Override
    public int optimizationsCount() {
        return ifStatementEliminatedCount + ternaryExpressionEliminatedCount
                + whileStatementEliminatedCount;
    }

    @Override
    public String summaryInfo() {
        if (optimizationsCount() == 0) return "";
        final StringBuilder sb = new StringBuilder();
        if (ifStatementEliminatedCount > 0) {
            sb.append("\nEliminated IfStatement: ").append(ifStatementEliminatedCount);
        }
        if (ternaryExpressionEliminatedCount > 0) {
            sb.append("\nEliminated TernaryExpression: ").append(ternaryExpressionEliminatedCount);
        }
        if (whileStatementEliminatedCount > 0) {
            sb.append("\nEliminated WhileStatement: ").append(whileStatementEliminatedCount);
        }
        return sb.toString();
    }

    @Override
    public Node visit(IfStatement s, Void t) {
        if (s.condition instanceof ValueExpression) {
            ifStatementEliminatedCount++;
            // true statement
            if (s.condition.eval().asInt() != 0) {
                return s.ifStatement;
            }
            // false statement
            if (s.elseStatement != null) {
                return s.elseStatement;
            }
            return new ExprStatement(s.condition);
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(TernaryExpression s, Void t) {
        if (s.condition instanceof ValueExpression) {
            ternaryExpressionEliminatedCount++;
            if (s.condition.eval().asInt() != 0) {
                return s.trueExpr;
            }
            return s.falseExpr;
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(WhileStatement s, Void t) {
        if (s.condition instanceof ValueExpression) {
            if (s.condition.eval().asInt() == 0) {
                whileStatementEliminatedCount++;
                return new ExprStatement(s.condition);
            }
        }
        return super.visit(s, t);
    }
}
