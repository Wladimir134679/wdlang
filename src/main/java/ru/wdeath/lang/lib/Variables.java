package ru.wdeath.lang.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Variables {

    private static Map<String, Value> variables;
    private static final Stack<Map<String, Value>> stack;

    static {
        variables = new HashMap<>();
        stack = new Stack<>();
        variables.put("PI", new NumberValue(Math.PI));
        variables.put("E", new NumberValue(Math.E));
    }

    public static void push(){
        stack.push(new HashMap<>(variables));
    }

    public static void pop(){
        variables = stack.pop();
    }

    public static Value get(String name){
        if(!isExists(name)) return NumberValue.ZERO;
        return variables.get(name);
    }

    public static boolean isExists(String name){
        return variables.containsKey(name);
    }

    public static void set(String name, Value value){
        variables.put(name, value);
    }
}
