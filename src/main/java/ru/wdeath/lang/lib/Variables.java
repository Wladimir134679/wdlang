package ru.wdeath.lang.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public class Variables {

    private static final Object lock = new Object();

    private static class Scope {
        public final Scope parent;
        public final Map<String, Value> variables;

        public Scope() {
            this(null);
        }

        public Scope(Scope parent) {
            this.parent = parent;
            variables = new ConcurrentHashMap<>();
        }
    }

    private static class ScopeFindData {
        public boolean isFound;
        public Scope scope;
    }

    private static volatile Scope scope;

    static {
        scope = new Scope();
        Variables.clear();
    }

    public static void clear(){
        scope.variables.clear();
        scope.variables.put("true", NumberValue.ONE);
        scope.variables.put("false", NumberValue.ZERO);
    }

    public static void push(){
        synchronized (lock) {
            final Scope newScope = new Scope(scope);
            scope = newScope;
        }
    }

    public static void pop(){
        synchronized (lock) {
            if (scope.parent != null) {
                scope = scope.parent;
            }
        }
    }

    public static Value get(String key){
        synchronized (lock) {
            final ScopeFindData scopeData = findScope(key);
            if (scopeData.isFound) {
                return scopeData.scope.variables.get(key);
            }
        }
        return NumberValue.ZERO;
    }

    public static void remove(String key) {
        synchronized (lock) {
            findScope(key).scope.variables.remove(key);
        }
    }

    public static boolean isExists(String name){
        synchronized (lock) {
            return findScope(name).isFound;
        }
    }

    public static void set(String name, Value value){
        synchronized (lock) {
            findScope(name).scope.variables.put(name, value);
        }
    }

    public static void define(String key, Value value) {
        synchronized (lock) {
            scope.variables.put(key, value);
        }
    }


    private static ScopeFindData findScope(String variable) {
        final ScopeFindData result = new ScopeFindData();

        Scope current = scope;
        do {
            if (current.variables.containsKey(variable)) {
                result.isFound = true;
                result.scope = current;
                return result;
            }
        } while ((current = current.parent) != null);

        result.isFound = false;
        result.scope = scope;
        return result;
    }
}
