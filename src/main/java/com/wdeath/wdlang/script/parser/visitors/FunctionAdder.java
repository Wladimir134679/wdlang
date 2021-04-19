package com.wdeath.wdlang.script.parser.visitors;

import com.wdeath.wdlang.script.parser.ast.FunctionDefineStatement;

public final class FunctionAdder extends AbstractVisitor {

    @Override
    public void visit(FunctionDefineStatement s) {
        super.visit(s);
        s.execute();
    }
}
