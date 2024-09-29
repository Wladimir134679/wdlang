package ru.wdeath.lang.module.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.module.ExpansionModule;

public class MathModule implements ExpansionModule {

    @Override
    public void init(ScopeHandler scope) {
        scope.setFunction("sin", new ProgramLibFunction(MathModule::sin));
        scope.setFunction("cos", new ProgramLibFunction(MathModule::cos));

        scope.setConstant("PI", NumberValue.of(Math.PI));
    }

    private static Value sin(ProgramContext pc, Value[] args) {
        ArgumentsUtil.check(1, args.length);
        return NumberValue.of(Math.sin(args[0].asDouble()));
    }

    private static Value cos(ProgramContext pc, Value[] args) {
        ArgumentsUtil.check(1, args.length);
        return NumberValue.of(Math.sin(args[0].asDouble()));
    }
}
