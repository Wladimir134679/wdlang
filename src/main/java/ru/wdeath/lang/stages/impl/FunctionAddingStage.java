package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.visitors.FunctionAdder;

public class FunctionAddingStage implements Stage<Statement, Statement> {

    @Override
    public Statement perform(StagesData stagesData, Statement input) {
        input.accept(new FunctionAdder());
        return input;
    }
}
