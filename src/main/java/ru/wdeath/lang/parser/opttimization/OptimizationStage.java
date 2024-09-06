package ru.wdeath.lang.parser.opttimization;

import ru.wdeath.lang.ast.Node;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.visitors.optimization.*;

public class OptimizationStage  implements Stage<Statement, Statement> {

    public static final String TAG_OPTIMIZATION_SUMMARY = "optimizationSummary";

    private final int level;
    private final boolean summary;
    private final Optimizable optimization;

    public OptimizationStage(int level) {
        this(level, false);
    }

    public OptimizationStage(int level, boolean summary) {
        this.level = level;
        this.summary = summary;
        optimization = new SummaryOptimization(new Optimizable[] {
                new ConstantFolding(),
                new ConstantPropagation(),
                new DeadCodeElimination(),
                new ExpressionSimplification()
        });
    }

    @Override
    public Statement perform(StagesData stagesData, Statement input) {
        if (level == 0) return input;

        Node result = input;
        final int maxIterations = level >= 9 ? Integer.MAX_VALUE : level;
        int lastModifications;
        int iteration = 0;
        do {
            lastModifications = optimization.optimizationsCount();
            result = optimization.optimize(result);
            iteration++;
        } while (lastModifications != optimization.optimizationsCount() && iteration < maxIterations);

        if (this.summary) {
            stagesData.put(TAG_OPTIMIZATION_SUMMARY, """
                Performed %d optimization iterations
                %s
                """.formatted(iteration, optimization.summaryInfo())
            );
        }

        return (Statement) result;
    }
}
