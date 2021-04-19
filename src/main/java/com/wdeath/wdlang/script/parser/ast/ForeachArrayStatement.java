package com.wdeath.wdlang.script.parser.ast;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.TypeException;
import com.wdeath.wdlang.script.lib.*;

import java.util.Map;

public final class ForeachArrayStatement extends InterruptableNode implements Statement {
    
    public final ScriptProgram scriptProgram;
    public final String variable;
    public final Expression container;
    public final Statement body;

    public ForeachArrayStatement(ScriptProgram scriptProgram, String variable, Expression container, Statement body) {
        this.scriptProgram = scriptProgram;
        this.variable = variable;
        this.container = container;
        this.body = body;
    }

    @Override
    public void execute() {
        super.interruptionCheck();
        final Value previousVariableValue = scriptProgram.getVariables().isExists(variable) ? scriptProgram.getVariables().get(variable) : null;

        final Value containerValue = container.eval();
        switch (containerValue.type()) {
            case Types.STRING:
                iterateString(containerValue.asString());
                break;
            case Types.ARRAY:
                iterateArray((ArrayValue) containerValue);
                break;
            case Types.MAP:
                iterateMap((MapValue) containerValue);
                break;
            default:
                throw new TypeException("Cannot iterate " + Types.typeToString(containerValue.type()));
        }

        // Restore variables
        if (previousVariableValue != null) {
            scriptProgram.getVariables().set(variable, previousVariableValue);
        } else {
            scriptProgram.getVariables().remove(variable);
        }
    }

    private void iterateString(String str) {
        for (char ch : str.toCharArray()) {
            scriptProgram.getVariables().set(variable, new StringValue(String.valueOf(ch)));
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }

    private void iterateArray(ArrayValue containerValue) {
        for (Value value : containerValue) {
            scriptProgram.getVariables().set(variable, value);
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }

    private void iterateMap(MapValue containerValue) {
        for (Map.Entry<Value, Value> entry : containerValue) {
            scriptProgram.getVariables().set(variable, new ArrayValue(new Value[] {
                    entry.getKey(),
                    entry.getValue()
            }));
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
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
        return String.format("for %s : %s %s", variable, container, body);
    }
}
