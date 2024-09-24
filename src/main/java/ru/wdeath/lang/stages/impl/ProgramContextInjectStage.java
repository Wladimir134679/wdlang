//package ru.wdeath.lang.stages.impl;
//
//import ru.wdeath.lang.ProgramContext;
//import ru.wdeath.lang.ast.Statement;
//import ru.wdeath.lang.stages.Stage;
//import ru.wdeath.lang.stages.StagesData;
//import ru.wdeath.lang.visitors.FunctionAdder;
//import ru.wdeath.lang.visitors.ProgramContextInjectVisitor;
//
//public class ProgramContextInjectStage implements Stage<Statement, Statement> {
//
//    private final ProgramContext programContext;
//
//    public ProgramContextInjectStage(ProgramContext programContext) {
//        this.programContext = programContext;
//    }
//
//    @Override
//    public Statement perform(StagesData stagesData, Statement input) {
//        input.accept(new ProgramContextInjectVisitor(programContext));
//        return input;
//    }
//}
