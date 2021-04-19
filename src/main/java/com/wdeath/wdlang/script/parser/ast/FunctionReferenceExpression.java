package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.lib.*;

public final class FunctionReferenceExpression extends InterruptableNode implements Expression {

    public final ScriptProgram scriptProgram;
    public final String name;

    public FunctionReferenceExpression(ScriptProgram scriptProgram, String name) {
        this.scriptProgram = scriptProgram;
        this.name = name;
    }

    @Override
    public FunctionValue eval() {
        super.interruptionCheck();
        return new FunctionValue(scriptProgram.getFunctions().get(name));
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
        return "::" + name;
    }
}
