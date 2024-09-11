package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.ast.Visitor;
import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LinterStage implements Stage<Statement, Statement> {

    public enum Mode { NONE, SEMANTIC, FULL }

    private final Mode mode;

    public final ProgramContext programContext;

    public LinterStage(Mode mode, ProgramContext programContext) {
        this.mode = mode;
        this.programContext = programContext;
    }

    @Override
    public Statement perform(StagesData stagesData, Statement input) {
        if (mode == Mode.NONE) return input;

        final LinterResults results = new LinterResults();
        final List<Visitor> validators = new ArrayList<>();
//        validators.add(new IncludeSourceValidator(results));
        validators.add(new LoopStatementsValidator(results));

        if (mode == Mode.SEMANTIC) {
            validators.forEach(input::accept);
            if (results.hasErrors()) {
                throw new WdlParserException(results.errors().toList());
            }
            return input;
        }

        // Full lint validation with Console output
        validators.add(new AssignValidator(results));
        validators.add(new DefaultFunctionsOverrideValidator(results, programContext));

        programContext.reset();
        for (Visitor validator : validators) {
            input.accept(validator);
            programContext.reset();
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
