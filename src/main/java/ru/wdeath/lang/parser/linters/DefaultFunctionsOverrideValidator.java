package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ast.FunctionDefineStatement;
import ru.wdeath.lang.lib.Functions;

public class DefaultFunctionsOverrideValidator extends LintVisitor {

    @Override
    public void visit(FunctionDefineStatement s) {
        super.visit(s);
        if (Functions.isExists(s.name)) {
            System.err.println(String.format(
                    "Warning: function \"%s\" overrides default module function", s.name));
        }
    }
}
