package ru.wdeath.lang.lib;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.UnknownFunctionException;
import ru.wdeath.lang.exception.WdlRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    public static void clearAndInit(ProgramContext programContext){
        ScopeHandler scope = programContext.getScope();
        scope.setFunction("println", (pc, v) -> {
            ArgumentsUtil.checkOrOr(0, 1, v.length);
            if (v.length == 1)
                pc.getConsole().println(v[0].asString());
            else
                pc.getConsole().println();
            return NumberValue.ZERO;
        });
        scope.setFunction("sin", (pc, v) -> {
            ArgumentsUtil.check(1, v.length);
            return NumberValue.of(Math.sin(v[0].asDouble()));
        });
        scope.setFunction("cos", (pc, v) -> {
            ArgumentsUtil.check(1, v.length);
            return NumberValue.of(Math.sin(v[0].asDouble()));
        });
        scope.setFunction("newarray", (pc, v) ->
                createArray(v, 0)
        );
        scope.setFunction("assertEquals", (pc, v) -> {
            ArgumentsUtil.check(2, v.length);
            if(!v[0].equals(v[1]))
                throw new WdlRuntimeException(v[0].asString() + " != " + v[1].asString());
            return NumberValue.ZERO;
        });
    }

    public static ArrayValue createArray(Value[] args, int index) {
        final var size = (int) args[index].asDouble();
        final var last = args.length - 1;
        ArrayValue array = new ArrayValue(size);
        if (index == last) {
            for (int i = 0; i < size; i++) {
                array.set(i, NumberValue.ZERO);
            }
        } else if (index < last) {
            for (int i = 0; i < size; i++) {
                array.set(i, createArray(args, index + 1));
            }
        }
        return array;
    }
}
