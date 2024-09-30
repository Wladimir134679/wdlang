package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.UnknownFunctionException;
import ru.wdeath.lang.lib.Scope.ScopeFindData;
import ru.wdeath.lang.lib.classes.ClassDeclaration;

import java.util.Map;

public class ScopeHandler {

    private final Object lock = new Object();

    private volatile RootScope rootScope;
    private volatile Scope scope;

    public ScopeHandler(){
        resetScope();
    }

    /**
     * Resets a scope for new program execution
     */
    public void resetScope() {
        rootScope = new RootScope();
        scope = rootScope;
    }

    public void push() {
        synchronized (lock) {
            scope = new Scope(scope);
        }
    }

    public void pop() {
        synchronized (lock) {
            if (!scope.isRoot()) {
                scope = scope.parent;
            }
        }
    }

    public void expansionScope(ScopeHandler other){
        rootScope.getFunctions().putAll(other.functions());
        rootScope.getConstants().putAll(other.constants());
        rootScope.getVariables().putAll(other.variables());
        rootScope.getClassDeclarations().putAll(other.classDeclarations());
    }

    public AutoCloseableScope closeableScope() {
        push();
        return new AutoCloseableScope(this);
    }

    public Map<String, Value> variables() {
        return scope.getVariables();
    }

    public Map<String, Value> constants() {
        return rootScope.getConstants();
    }

    public Map<String, Function> functions() {
        return rootScope.getFunctions();
    }

    public Map<String, ClassDeclaration> classDeclarations() {
        return rootScope.getClassDeclarations();
    }

    public boolean isConstantExists(String name) {
        return rootScope.containsConstant(name);
    }

    public boolean isFunctionExists(String name) {
        return rootScope.containsFunction(name);
    }

    public Function getFunction(String name) {
        final var function = rootScope.getFunction(name);
        if (function == null) throw new UnknownFunctionException(name);
        return function;
    }

    public void setFunction(String name, Function function) {
        rootScope.setFunction(name, function);
    }

    public ClassDeclaration getClassDeclaration(String name) {
        return rootScope.getClassDeclaration(name);
    }

    public void setClassDeclaration(ClassDeclaration classDeclaration) {
        rootScope.setClassDeclaration(classDeclaration);
    }

    public boolean isVariableOrConstantExists(String name) {
        Value constant = rootScope.getConstant(name);
        if (constant != null) {
            return true;
        }
        synchronized (lock) {
            return findScope(name).isFound;
        }
    }

    public Value getVariableOrConstant(String name) {
        Value constant = rootScope.getConstant(name);
        if (constant != null) {
            return constant;
        }
        synchronized (lock) {
            final ScopeFindData scopeData = findScope(name);
            if (scopeData.isFound) {
                return scopeData.scope.get(name);
            }
        }
        return NumberValue.ZERO;
    }

    public Value getVariable(String name) {
        synchronized (lock) {
            final ScopeFindData scopeData = findScope(name);
            if (scopeData.isFound) {
                return scopeData.scope.getVariable(name);
            }
        }
        // TODO should be strict
        return NumberValue.ZERO;
    }

    public void setVariable(String name, Value value) {
        synchronized (lock) {
            findScope(name).scope.setVariable(name, value);
        }
    }

    public void setConstant(String name, Value value) {
        rootScope.setConstant(name, value);
    }

    public void defineVariableInCurrentScope(String name, Value value) {
        synchronized (lock) {
            scope.setVariable(name, value);
        }
    }

    private ScopeFindData findScope(String name) {
        Scope current = scope;
        do {
            if (current.contains(name)) {
                return new ScopeFindData(true, current);
            }
        } while ((current = current.parent) != null);

        return new ScopeFindData(false, scope);
    }
}
