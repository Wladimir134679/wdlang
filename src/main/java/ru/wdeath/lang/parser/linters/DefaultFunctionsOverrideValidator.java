package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.ast.FunctionDefineStatement;
import ru.wdeath.lang.lib.ScopeHandler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultFunctionsOverrideValidator extends LintVisitor {

    private final Set<String> moduleFunctions = new HashSet<>();
    private final ProgramContext programContext;

    public DefaultFunctionsOverrideValidator(LinterResults results, ProgramContext programContext) {
        super(results);
        this.programContext = programContext;
    }

    @Override
    public void visit(FunctionDefineStatement s) {
        super.visit(s);
        if (programContext.getScope().isVariableOrConstantExists(s.name)) {
            System.err.println(String.format(
                    "Warning: function \"%s\" overrides default module function", s.name));
        }
    }
}
