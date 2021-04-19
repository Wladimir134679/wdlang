package com.wdeath.wdlang.script.lib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Variables {

    private static final Object lock = new Object();

    private static class Scope {
        final Scope parent;
        final Map<String, Value> variables;

        Scope() {
            this(null);
        }

        Scope(Scope parent) {
            this.parent = parent;
            variables = new ConcurrentHashMap<>();
        }
    }

    private static class ScopeFindData {
        boolean isFound;
        Scope scope;
    }

    private volatile Scope scope;

    public Variables() {
        clear();
    }

    public Map<String, Value> variables() {
        return scope.variables;
    }

    public void clear() {
        scope = new Scope();
        scope.variables.clear();
        scope.variables.put("true", NumberValue.ONE);
        scope.variables.put("false", NumberValue.ZERO);
    }
    
    public void push() {
        synchronized (lock) {
            scope = new Scope(scope);
        }
    }
    
    public void pop() {
        synchronized (lock) {
            if (scope.parent != null) {
                scope = scope.parent;
            }
        }
    }
    
    public boolean isExists(String key) {
        synchronized (lock) {
            return findScope(key).isFound;
        }
    }
    
    public Value get(String key) {
        synchronized (lock) {
            final ScopeFindData scopeData = findScope(key);
            if (scopeData.isFound) {
                return scopeData.scope.variables.get(key);
            }
        }
        return NumberValue.ZERO;
    }
    
    public void set(String key, Value value) {
        synchronized (lock) {
            findScope(key).scope.variables.put(key, value);
        }
    }
    
    public void define(String key, Value value) {
        synchronized (lock) {
            scope.variables.put(key, value);
        }
    }

    public void remove(String key) {
        synchronized (lock) {
            findScope(key).scope.variables.remove(key);
        }
    }

    /*
     * Find scope where variable exists.
     */
    private ScopeFindData findScope(String variable) {
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
