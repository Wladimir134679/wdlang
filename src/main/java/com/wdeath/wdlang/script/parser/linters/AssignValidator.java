package com.wdeath.wdlang.script.parser.linters;


import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.parser.ast.AssignmentExpression;
import com.wdeath.wdlang.script.parser.ast.IncludeStatement;
import com.wdeath.wdlang.script.parser.ast.UseStatement;
import com.wdeath.wdlang.script.parser.ast.VariableExpression;

public final class AssignValidator extends LintVisitor {

    private final ScriptProgram scriptProgram;

    public AssignValidator(ScriptProgram scriptProgram) {
        this.scriptProgram = scriptProgram;
    }

    @Override
    public void visit(AssignmentExpression s) {
        super.visit(s);
        if (s.target instanceof VariableExpression) {
            final String variable = ((VariableExpression) s.target).name;
            if (scriptProgram.getVariables().isExists(variable)) {
                System.err.println(String.format(
                        "Warning: variable \"%s\" overrides constant", variable));;
            }
        }
    }

    @Override
    public void visit(IncludeStatement st) {
        super.visit(st);
        applyVisitor(st, this);
    }

    @Override
    public void visit(UseStatement st) {
        super.visit(st);
        st.execute();
    }
}
