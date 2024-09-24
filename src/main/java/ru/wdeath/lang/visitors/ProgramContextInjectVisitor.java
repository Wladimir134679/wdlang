//package ru.wdeath.lang.visitors;
//
//import ru.wdeath.lang.ProgramContext;
//import ru.wdeath.lang.ast.*;
//
//public class ProgramContextInjectVisitor extends AbstractVisitor {
//
//    private final ProgramContext programContext;
//
//    public ProgramContextInjectVisitor(ProgramContext programContext) {
//        this.programContext = programContext;
//    }
//
//    @Override
//    public void visit(ClassDeclarationStatement st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(ContainerAccessExpression st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(ForeachMapStatement st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(ForeachArrayStatement st) {
//        st.context = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(FunctionReferenceExpression st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(FunctionExpression st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(FunctionDefineStatement st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(MatchExpression st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(ObjectCreationExpression st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(VariableExpression st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(PrintStatement st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//
//    @Override
//    public void visit(ImportStatement st) {
//        st.programContext = programContext;
//        super.visit(st);
//    }
//}
