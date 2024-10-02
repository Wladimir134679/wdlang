package ru.wdeath.lang.module.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.module.ExpansionModule;
import ru.wdeath.lang.module.InitModule;
import ru.wdeath.lang.module.NameExpansionModule;

public class STDModule implements NameExpansionModule {

    @Override
    public String name() {
        return "std";
    }

    @Override
    public void init(InitModule init) {
        init
                .add("len", STDModule::len)
                .add("try", STDModule::tryFunc);
    }

    private static Value tryFunc(ProgramContext pc, Value[] args) {
        ArgumentsUtil.checkOrOr(1, 2, args.length);
        try {
            return ValueUtils.consumeFunction(args[0], 0).execute();
        } catch (Exception e) {
            if (args.length == 2 && args[1].type() == Types.FUNCTION) {
                String message = e.getMessage();
                return ((FunctionValue) args[1]).getFunction()
                        .execute(new StringValue(e.getClass().getName()),
                                new StringValue(message));
            }
            return NumberValue.MINUS_ONE;
        }
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
