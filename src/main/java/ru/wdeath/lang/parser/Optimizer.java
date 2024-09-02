package ru.wdeath.lang.parser;

import ru.wdeath.lang.ast.Node;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.visitors.optimization.*;

public class Optimizer {

    public static Statement optimize(Statement statement, int level) {
        if (level == 0) return statement;

        final Optimizable optimization = new SummaryOptimization(new Optimizable[]{
                new ConstantFolding(),
                new ConstantPropagation(),
                new DeadCodeElimination(),
                new ExpressionSimplification()
        });

        Node result = statement;
        try {

            int iteration = 0, lastModifications = 0;
            do {
                lastModifications = optimization.optimizationsCount();
                result = optimization.optimize(result);
                iteration++;
            } while (lastModifications != optimization.optimizationsCount() && iteration < level);
            System.out.println("Performs " + iteration + " optimization iterations");

            return (Statement) result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            System.out.println(result);
            System.out.println(optimization.summaryInfo());
        }

    }
}
