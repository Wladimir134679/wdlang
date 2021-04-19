package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.lib.*;

public final class FunctionDefineStatement implements Statement {

    public final ScriptProgram scriptProgram;
    public final String name;
    public final Arguments arguments;
    public final Statement body;
    
    public FunctionDefineStatement(ScriptProgram scriptProgram, String name, Arguments arguments, Statement body) {
        this.scriptProgram = scriptProgram;
        this.name = name;
        this.arguments = arguments;
        this.body = body;
    }

    @Override
    public void execute() {
        scriptProgram.getFunctions().set(name, new UserDefinedFunction(scriptProgram, arguments, body));
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
        if (body instanceof ReturnStatement) {
            return String.format("def %s%s = %s", name, arguments, ((ReturnStatement)body).expression);
        }
        return String.format("def %s%s %s", name, arguments, body);
    }
}
