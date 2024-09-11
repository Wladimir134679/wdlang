package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.visitors.MermaidchartVisitor;

public class MermaidStage implements Stage<Statement, Statement>  {

    @Override
    public Statement perform(StagesData stagesData, Statement input) {
        MermaidchartVisitor visitor = new MermaidchartVisitor();
        input.accept(visitor);
        System.out.println("=".repeat(10));
        visitor.print();
        System.out.println("=".repeat(10));
        return input;
    }
}
