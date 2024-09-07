package ru.wdeath.lang.visitors;

import ru.wdeath.lang.ast.*;

public class FunctionAdder extends AbstractVisitor {

    @Override
    public void visit(FunctionDefineStatement st) {
        super.visit(st);
        st.eval();
    }
}
