package ru.wdeath.lang.visitors.optimization;

import ru.wdeath.lang.ast.Node;

public class SummaryOptimization implements Optimizable {

    private final Optimizable[] optimizations;

    public SummaryOptimization(Optimizable[] optimizations) {
        this.optimizations = optimizations;
    }

    @Override
    public Node optimize(Node node) {
        for (Optimizable optimization : optimizations) {
            node = optimization.optimize(node);
        }
        return node;
    }

    @Override
    public int optimizationsCount() {
        int count = 0;
        for (Optimizable optimization : optimizations) {
            count += optimization.optimizationsCount();
        }
        return count;
    }

    @Override
    public String summaryInfo() {
        final StringBuilder sb = new StringBuilder();
        for (Optimizable optimization : optimizations) {
            sb.append(optimization.summaryInfo());
        }
        return sb.toString();
    }
}
