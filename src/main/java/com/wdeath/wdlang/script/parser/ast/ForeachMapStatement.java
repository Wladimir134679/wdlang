package com.wdeath.wdlang.script.parser.ast;


import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.TypeException;
import com.wdeath.wdlang.script.lib.*;

import java.util.Map;

public final class ForeachMapStatement extends InterruptableNode implements Statement {

    public final ScriptProgram scriptProgram;
    public final String key, value;
    public final Expression container;
    public final Statement body;

    public ForeachMapStatement(ScriptProgram scriptProgram, String key, String value, Expression container, Statement body) {
        this.scriptProgram = scriptProgram;
        this.key = key;
        this.value = value;
        this.container = container;
        this.body = body;
    }

    @Override
    public void execute() {
        super.interruptionCheck();
        final Value previousVariableValue1 = scriptProgram.getVariables().isExists(key) ? scriptProgram.getVariables().get(key) : null;
        final Value previousVariableValue2 = scriptProgram.getVariables().isExists(value) ? scriptProgram.getVariables().get(value) : null;

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
                throw new TypeException("Cannot iterate " + Types.typeToString(containerValue.type()) + " as key, value pair");
        }

        // Restore variables
        if (previousVariableValue1 != null) {
            scriptProgram.getVariables().set(key, previousVariableValue1);
        } else {
            scriptProgram.getVariables().remove(key);
        }
        if (previousVariableValue2 != null) {
            scriptProgram.getVariables().set(value, previousVariableValue2);
        } else {
            scriptProgram.getVariables().remove(value);
        }
    }

    private void iterateString(String str) {
        for (char ch : str.toCharArray()) {
            scriptProgram.getVariables().set(key, new StringValue(String.valueOf(ch)));
            scriptProgram.getVariables().set(value, NumberValue.of(ch));
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
        int index = 0;
        for (Value v : containerValue) {
            scriptProgram.getVariables().set(key, v);
            scriptProgram.getVariables().set(value, NumberValue.of(index++));
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
            scriptProgram.getVariables().set(key, entry.getKey());
            scriptProgram.getVariables().set(value, entry.getValue());
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
        return String.format("for %s, %s : %s %s", key, value, container, body);
    }
}
