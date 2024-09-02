package ru.wdeath.lang.parser;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.visitors.ConstantFolding;
import ru.wdeath.lang.visitors.DeadCodeElimination;

public class Optimizer {

    public interface Info {

        int optimizationsCount();

        String summaryInfo();
    }

    public static Statement optimize(Statement statement, int level) {
        if (level == 0) return statement;

        final ConstantFolding constantFolding = new ConstantFolding();
        final DeadCodeElimination deadCodeElimination = new DeadCodeElimination();

        Statement result = statement;
        for (int i = 0; i < level; i++) {
            result = (Statement) result.accept(constantFolding, null);
            result = (Statement) result.accept(deadCodeElimination, null);
        }
        System.out.print(constantFolding.summaryInfo());
        System.out.print(deadCodeElimination.summaryInfo());
        System.out.println();
        return result;
    }
}