package com.wdeath.wdlang.script.parser.linters;

import com.wdeath.wdlang.script.parser.ast.IncludeStatement;
import com.wdeath.wdlang.script.parser.ast.Statement;
import com.wdeath.wdlang.script.parser.ast.Visitor;
import com.wdeath.wdlang.script.parser.visitors.AbstractVisitor;
import com.wdeath.wdlang.script.parser.visitors.VisitorUtils;

public abstract class LintVisitor extends AbstractVisitor {

    protected void applyVisitor(IncludeStatement s, Visitor visitor) {
        final Statement program = VisitorUtils.includeProgram(s);
        if (program != null) {
            program.accept(visitor);
        }
    }
}
