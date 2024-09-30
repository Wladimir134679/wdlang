package ru.wdeath.lang.module;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.lib.classes.ClassDeclaration;

public class InitModule {

    private final ProgramContext programContext;

    public InitModule(ProgramContext programContext) {
        this.programContext = programContext;
    }

    public InitModule add(String name, ProgramLibFunction.FuncProgram func){
        return add(name, new ProgramLibFunction(programContext, func));
    }

    public InitModule add(String name, Value value){
        scope().setVariable(name, value);
        return this;
    }

    public InitModule add(String name, Function function){
        scope().setFunction(name, function);
        return this;
    }

    public InitModule add(ClassDeclaration classDeclaration){
        scope().setClassDeclaration(classDeclaration);
        return this;
    }

    public InitModule addConst(String name, Value value){
        scope().setConstant(name, value);
        return this;
    }

    public ProgramContext programContext() {
        return programContext;
    }

    public ScopeHandler scope(){
        return programContext.getScope();
    }
}
