package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ast.AssignmentExpression;
import ru.wdeath.lang.ast.VariableExpression;
import ru.wdeath.lang.lib.Variables;

public class AssignValidator extends LintVisitor {

    @Override
    public void visit(AssignmentExpression s) {
        super.visit(s);
        if (s.target instanceof VariableExpression) {
            final String variable = ((VariableExpression) s.target).name;
            if (Variables.isExists(variable)) {
                System.err.println(String.format(
                        "Warning: variable \"%s\" overrides constant", variable));
            }
        }
    }

}
