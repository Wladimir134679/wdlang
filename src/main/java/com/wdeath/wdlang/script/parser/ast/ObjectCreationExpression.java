package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.UnknownClassException;
import com.wdeath.wdlang.script.lib.*;
import java.util.Iterator;
import java.util.List;

public final class ObjectCreationExpression implements Expression {

    public final ScriptProgram scriptProgram;
    public final String className;
    public final List<Expression> constructorArguments;

    public ObjectCreationExpression(ScriptProgram scriptProgram, String className, List<Expression> constructorArguments) {
        this.scriptProgram = scriptProgram;
        this.className = className;
        this.constructorArguments = constructorArguments;
    }
    
    @Override
    public Value eval() {
        final ClassDeclarationStatement cd = scriptProgram.getClasses().get(className);
        if (cd == null) {
            // Is Instantiable?
            if (scriptProgram.getVariables().isExists(className)) {
                final Value variable = scriptProgram.getVariables().get(className);
                if (variable instanceof Instantiable) {
                    return ((Instantiable) variable).newInstance(ctorArgs());
                }
            }
            throw new UnknownClassException(className);
        }
        
        // Create an instance and put evaluated fields with method declarations
        final ClassInstanceValue instance = new ClassInstanceValue(className);
        for (AssignmentExpression f : cd.fields) {
            // TODO check only variable assignments
            final String fieldName = ((VariableExpression) f.target).name;
            instance.addField(fieldName, f.eval());
        }
        for (FunctionDefineStatement m : cd.methods) {
            instance.addMethod(m.name, new ClassMethod(scriptProgram, m.body, m.arguments, instance));
        }
        
        // Call a constructor
        instance.callConstructor(ctorArgs());
        return instance;
    }
    
    private Value[] ctorArgs() {
        final int argsSize = constructorArguments.size();
        final Value[] ctorArgs = new Value[argsSize];
        for (int i = 0; i < argsSize; i++) {
            ctorArgs[i] = constructorArguments.get(i).eval();
        }
        return ctorArgs;
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
        final StringBuilder sb = new StringBuilder();
        sb.append("new ").append(className).append(' ');
        final Iterator<Expression> it = constructorArguments.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(", ").append(it.next());
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
