package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.UnknownFunctionException;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    public static void clearAndInit(){
        addFunction("println", v -> {
            ArgumentsUtil.checkOrOr(0, 1, v.length);
            if (v.length == 1)
                System.out.println(v[0].asString());
            else
                System.out.println();
            return NumberValue.ZERO;
        });
        addFunction("sin", v -> {
            ArgumentsUtil.check(1, v.length);
            return NumberValue.of(Math.sin(v[0].asDouble()));
        });
        addFunction("cos", v -> {
            ArgumentsUtil.check(1, v.length);
            return NumberValue.of(Math.sin(v[0].asDouble()));
        });
        addFunction("newarray", v ->
                createArray(v, 0)
        );
        addFunction("assertEquals", v -> {
            ArgumentsUtil.check(2, v.length);
            if(!v[0].equals(v[1]))
                throw new RuntimeException(v[0].asString() + " != " + v[1].asString());
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

    public static boolean isExists(String name) {
        return ScopeHandler.isFunctionExists(name);
    }

    public static Function getFunction(String name) {
        return ScopeHandler.getFunction(name);
    }

    public static void addFunction(String name, Function function) {
        ScopeHandler.setFunction(name, function);
    }
}
