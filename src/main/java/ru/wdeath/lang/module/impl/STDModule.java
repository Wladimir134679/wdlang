package ru.wdeath.lang.module.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.module.ExpansionModule;
import ru.wdeath.lang.module.InitModule;

public class STDModule implements ExpansionModule {

    @Override
    public void init(InitModule init) {
        init.add("len", STDModule::len);
    }

    private static Value len(ProgramContext pc, Value[] args) {
        ArgumentsUtil.check(1, args.length);
        Value val = args[0];
        int size = switch (val.type()) {
            case Types.STRING -> val.asString().length();
            case Types.ARRAY -> ((ArrayValue) val).size();
            case Types.MAP -> ((MapValue) val).size();
            default -> -1;
        };
        return NumberValue.of(size);
    }
}
