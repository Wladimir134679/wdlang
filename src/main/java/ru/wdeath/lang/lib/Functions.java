package ru.wdeath.lang.lib;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    private static final Map<String, Function> functions;

    static {
        functions = new HashMap<>();
        functions.put("println", v -> {
            if(v.length != 1) throw new RuntimeException("Error args");
            System.out.println(v[0].asString());
            return NumberValue.ZERO;
        });
        functions.put("sin", v -> {
            if(v.length != 1) throw new RuntimeException("Error args");
            return new NumberValue(Math.sin(v[0].asDouble()));
        });
        functions.put("cos", v -> {
            if(v.length != 1) throw new RuntimeException("Error args");
            return new NumberValue(Math.sin(v[0].asDouble()));
        });
    }

    public static boolean isExists(String name){
        return functions.containsKey(name);
    }

    public static Function getFunction(String name){
        if(!isExists(name)) throw new RuntimeException("Function not found");
        return functions.get(name);
    }

    public static void addFunction(String name, Function function){
        functions.put(name, function);
    }
}
