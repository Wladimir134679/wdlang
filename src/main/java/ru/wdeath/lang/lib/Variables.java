package ru.wdeath.lang.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class Variables {

    private Variables() {
    }

    public static Map<String, Value> variables() {
        return ScopeHandler.variables();
    }

    public static boolean isExists(String name) {
        return ScopeHandler.isVariableOrConstantExists(name);
    }

    public static Value get(String name) {
        return ScopeHandler.getVariableOrConstant(name);
    }

    public static void set(String name, Value value) {
        ScopeHandler.setVariable(name, value);
    }

    public static void define(String name, Value value) {
        ScopeHandler.setConstant(name, value);
    }
}
