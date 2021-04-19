package com.wdeath.wdlang.script.parser.visitors;

import com.wdeath.wdlang.script.parser.ast.ClassDeclarationStatement;

public class ClassAdder extends AbstractVisitor {

    @Override
    public void visit(ClassDeclarationStatement s) {
        super.visit(s);
        s.execute();
    }
}
