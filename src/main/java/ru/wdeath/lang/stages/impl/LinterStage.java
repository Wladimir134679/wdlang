package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.ast.Visitor;
import ru.wdeath.lang.lib.Function;
import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.parser.linters.AssignValidator;
import ru.wdeath.lang.parser.linters.DefaultFunctionsOverrideValidator;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LinterStage implements Stage<Statement, Statement> {

    @Override
    public Statement perform(StagesData stagesData, Statement input) {
        final List<LinterResult> results = new ArrayList<>();
        final Visitor[] validators = new Visitor[] {
                new AssignValidator(results),
                new DefaultFunctionsOverrideValidator(results)
        };

        ScopeHandler.resetScope();
        for (Visitor validator : validators) {
            input.accept(validator);
            ScopeHandler.resetScope();
        }

        results.sort(Comparator.comparing(LinterResult::severity));
        System.out.println(String.format("Lint validation completed. %d results found!", results.size()));
        for (LinterResult r : results) {
            switch (r.severity()) {
                case ERROR -> System.err.println(r);
                case WARNING -> System.out.println(r);
            }
        }
        return input;
    }
}
