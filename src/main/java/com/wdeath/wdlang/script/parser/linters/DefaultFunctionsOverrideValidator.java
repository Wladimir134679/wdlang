package com.wdeath.wdlang.script.parser.linters;


import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.parser.ast.FunctionDefineStatement;
import com.wdeath.wdlang.script.parser.ast.IncludeStatement;
import com.wdeath.wdlang.script.parser.ast.UseStatement;

public final class DefaultFunctionsOverrideValidator extends LintVisitor {

    private ScriptProgram scriptProgram;

    public DefaultFunctionsOverrideValidator(ScriptProgram scriptProgram) {
        this.scriptProgram = scriptProgram;
    }

    @Override
    public void visit(FunctionDefineStatement s) {
        super.visit(s);
        if (scriptProgram.getFunctions().isExists(s.name)) {
            System.err.println(String.format(
                    "Warning: function \"%s\" overrides default module function", s.name));
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
