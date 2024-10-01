package ru.wdeath.lang.module.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.module.ExpansionModule;
import ru.wdeath.lang.module.InitModule;
import ru.wdeath.lang.module.NameExpansionModule;

public class MathModule implements NameExpansionModule {

    @Override
    public String name() {
        return "math";
    }

    @Override
    public void init(InitModule init) {
        init
                .add("sin", MathModule::sin)
                .add("cos", MathModule::cos)
                .addConst("PI", NumberValue.of(Math.PI));
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
