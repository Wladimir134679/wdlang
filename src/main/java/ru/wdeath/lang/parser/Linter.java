package ru.wdeath.lang.parser;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.ast.Visitor;
import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.Variables;
import ru.wdeath.lang.parser.linters.AssignValidator;
import ru.wdeath.lang.parser.linters.DefaultFunctionsOverrideValidator;
import ru.wdeath.lang.parser.linters.UseWithNonStringValueValidator;

public class Linter {

    public static void lint(Statement program) {
        new Linter(program).execute();
    }

    private final Statement program;

    private Linter(Statement program) {
        this.program = program;
    }

    public void execute() {
        final Visitor[] validators = new Visitor[] {
                new UseWithNonStringValueValidator(),
                new AssignValidator(),
                new DefaultFunctionsOverrideValidator()
        };
        resetState();
        for (Visitor validator : validators) {
            program.accept(validator);
            resetState();
        }
        System.out.println("Lint validation complete!");
    }

    private void resetState() {
        Variables.clear();
        Functions.clearAndInit();
    }
}
