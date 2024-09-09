package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.ast.AssignmentExpression;
import ru.wdeath.lang.ast.VariableExpression;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AssignValidator extends LintVisitor {

    private final Set<String> moduleConstants = new HashSet<>();

    public AssignValidator(LinterResults results) {
        super(results);
    }

    @Override
    public void visit(AssignmentExpression s) {
        super.visit(s);
        if (s.target instanceof VariableExpression) {
            final String variable = ((VariableExpression) s.target).name;
            if (moduleConstants.contains(variable)) {
                results.add(new LinterResult(LinterResult.Severity.WARNING,
                        String.format("Variable \"%s\" overrides constant", variable)));
            }
        }
    }

}
