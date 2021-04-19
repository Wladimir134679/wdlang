package com.wdeath.wdlang.script.parser;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.parser.ast.Node;
import com.wdeath.wdlang.script.parser.optimization.*;

public final class Optimizer {

    private Optimizer() { }

    public static ScriptProgram optimize(ScriptProgram scriptProgram, int level, boolean showSummary) {
        if (level == 0) return scriptProgram;

        final Optimizable optimization = new SummaryOptimization(new Optimizable[] {
            new ConstantFolding(scriptProgram),
            new ConstantPropagation(scriptProgram),
            new DeadCodeElimination(scriptProgram),
            new ExpressionSimplification(scriptProgram),
            new InstructionCombining(scriptProgram)
        });

        Node result = scriptProgram.getRootStatement();
        if (level >= 9) {
            int iteration = 0, lastModifications = 0;
            do {
                lastModifications = optimization.optimizationsCount();
                result = optimization.optimize(result);
                iteration++;
            } while (lastModifications != optimization.optimizationsCount());
            if (showSummary)
                System.out.print("Performs " + iteration + " optimization iterations");
        } else {
            for (int i = 0; i < level; i++) {
                result = optimization.optimize(result);
            }
        }
        if (showSummary) {
            System.out.println(optimization.summaryInfo());
        }
        return scriptProgram;
    }
}
