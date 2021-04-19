package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.TypeException;
import com.wdeath.wdlang.script.lib.*;
import com.wdeath.wdlang.script.modules.Module;

import java.lang.reflect.Method;

public final class UseStatement extends InterruptableNode implements Statement {

    private static final String PACKAGE = "com.annimon.ownlang.modules.%s.%s";
    private static final String INIT_CONSTANTS_METHOD = "initConstants";

    public final ScriptProgram scriptProgram;
    public final Expression expression;
    
    public UseStatement(ScriptProgram scriptProgram, Expression expression) {
        this.scriptProgram = scriptProgram;
        this.expression = expression;
    }
    
    @Override
    public void execute() {
        super.interruptionCheck();
        final Value value = expression.eval();
        switch (value.type()) {
            case Types.ARRAY:
                for (Value module : ((ArrayValue) value)) {
                    loadModule(module.asString());
                }
                break;
            case Types.STRING:
                loadModule(value.asString());
                break;
            default:
                throw typeException(value);
        }
    }

    private void loadModule(String name) {
        try {
            var module = scriptProgram.getModules().newInstance(name);
            if(module == null)
                module = scriptProgram.getModules().getObj(name);
            if(module == null)
                throw new RuntimeException("Unable to load module " + name);
            module.init(scriptProgram);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load module " + name, ex);
        }
    }

    public void loadConstants() {
        if (expression instanceof ArrayExpression) {
            ArrayExpression ae = (ArrayExpression) expression;
            for (Expression expr : ae.elements) {
                loadConstants(expr.eval().asString());
            }
        }
        if (expression instanceof ValueExpression) {
            ValueExpression ve = (ValueExpression) expression;
            loadConstants(ve.value.asString());
        }
    }

    private TypeException typeException(Value value) {
        return new TypeException("Array or string required in 'use' statement, " +
                "got " + Types.typeToString(value.type()) + " " + value);
    }

    private void loadConstants(String moduleName) {
        try {
            final Class<?> moduleClass = Class.forName(String.format(PACKAGE, moduleName, moduleName));
            final Method method = moduleClass.getMethod(INIT_CONSTANTS_METHOD);
            if (method != null) {
                method.invoke(this);
            }
        } catch (Exception ex) {
            // ignore
        }
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return "use " + expression;
    }
}
