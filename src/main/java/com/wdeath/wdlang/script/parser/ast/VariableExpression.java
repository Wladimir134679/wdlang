package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.VariableDoesNotExistsException;
import com.wdeath.wdlang.script.lib.*;

public final class VariableExpression extends InterruptableNode implements Expression, Accessible {

    public final ScriptProgram scriptProgram;
    public final String name;
    
    public VariableExpression(ScriptProgram scriptProgram, String name) {
        this.scriptProgram = scriptProgram;
        this.name = name;
    }

    @Override
    public Value eval() {
        super.interruptionCheck();
        return get();
    }
    
    @Override
    public Value get() {
        if (!scriptProgram.getVariables().isExists(name)) throw new VariableDoesNotExistsException(name);
        return scriptProgram.getVariables().get(name);
    }

    @Override
    public Value set(Value value) {
        scriptProgram.getVariables().set(name, value);
        return value;
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
        return name;
    }
}
