package com.wdeath.wdlang.script.parser;


import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.parser.ast.*;
import com.wdeath.wdlang.script.parser.linters.*;

public final class Linter {

    public static void lint(ScriptProgram scriptProgram) {
        new Linter(scriptProgram).execute();
    }

    private final ScriptProgram scriptProgram;

    private Linter(ScriptProgram scriptProgram) {
        this.scriptProgram = scriptProgram;
    }

    public void execute() {
        final Visitor[] validators = new Visitor[] {
            new UseWithNonStringValueValidator(),
            new AssignValidator(scriptProgram),
            new DefaultFunctionsOverrideValidator(scriptProgram)
        };
        resetState();
        for (Visitor validator : validators) {
            scriptProgram.getRootStatement().accept(validator);
            resetState();
        }
        System.out.println("Lint validation complete!");
    }

    private void resetState() {
        scriptProgram.getVariables().clear();
        scriptProgram.getFunctions().clear();
    }
}
