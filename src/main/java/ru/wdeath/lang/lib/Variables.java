package ru.wdeath.lang.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class Variables {

    private static Map<String, Value> variables;
    private static final Stack<Map<String, Value>> stack;

    static {
        stack = new Stack<>();
        variables = new ConcurrentHashMap<>();
        variables.put("true", NumberValue.ONE);
        variables.put("false", NumberValue.ZERO);
    }

    public static void push(){
        stack.push(new ConcurrentHashMap<>(variables));
    }

    public static void pop(){
        variables = stack.pop();
    }

    public static Value get(String name){
        if(!isExists(name)) return NumberValue.ZERO;
        return variables.get(name);
    }

    public static void remove(String key) {
        variables.remove(key);
    }

    public static boolean isExists(String name){
        return variables.containsKey(name);
    }

    public static void set(String name, Value value){
        variables.put(name, value);
    }
}
