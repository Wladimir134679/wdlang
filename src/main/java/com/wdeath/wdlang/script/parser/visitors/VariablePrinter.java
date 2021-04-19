package com.wdeath.wdlang.script.parser.visitors;

import com.wdeath.wdlang.script.parser.ast.*;

public final class VariablePrinter extends AbstractVisitor {

    @Override
    public void visit(AssignmentExpression s) {
        super.visit(s);
        System.out.println(s.target);
    }

    @Override
    public void visit(VariableExpression s) {
        super.visit(s);
        System.out.println(s.name);
    }
}
