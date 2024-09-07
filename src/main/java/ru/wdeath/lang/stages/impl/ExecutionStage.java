package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

public class ExecutionStage implements Stage<Statement, Statement> {

    @Override
    public Statement perform(StagesData stagesData, Statement input) {
        input.eval();
        return input;
    }
}
