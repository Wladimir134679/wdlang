package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ast.FunctionDefineStatement;
import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.stages.impl.LinterResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultFunctionsOverrideValidator extends LintVisitor {

    private final Set<String> moduleFunctions = new HashSet<>();

    public DefaultFunctionsOverrideValidator(Collection<LinterResult> results) {
        super(results);
    }

    @Override
    public void visit(FunctionDefineStatement s) {
        super.visit(s);
        if (ScopeHandler.isVariableOrConstantExists(s.name)) {
            System.err.println(String.format(
                    "Warning: function \"%s\" overrides default module function", s.name));
        }
    }
}
