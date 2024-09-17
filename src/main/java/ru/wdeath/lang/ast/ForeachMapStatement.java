package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.*;

import java.util.Map;

public class ForeachMapStatement extends InterruptableNode implements Statement {

    public ProgramContext programContext;
    public final String key, value;
    public final Node container;
    public final Statement body;

    public ForeachMapStatement(String key, String value, Node container, Statement body) {
        this.key = key;
        this.value = value;
        this.container = container;
        this.body = body;
    }

    @Override
    public Value eval() {
        super.interruptionCheck();
        try (final var ignored = programContext.getScope().closeableScope()) {
            final Value containerValue = container.eval();
            switch (containerValue.type()) {
                case Types.STRING -> iterateString(containerValue.asString());
                case Types.ARRAY -> iterateArray((ArrayValue) containerValue);
                case Types.MAP -> iterateMap((MapValue) containerValue);
                default -> throw new TypeException("Cannot iterate " + Types.typeToString(containerValue.type()) + " as key, value pair");
            }
        }
        return NumberValue.ZERO;
    }

    private void iterateString(String str) {
        for (char ch : str.toCharArray()) {
            programContext.getScope().setVariable(key, new StringValue(String.valueOf(ch)));
            programContext.getScope().setVariable(value, NumberValue.of(ch));
            try {
                body.eval();
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
            programContext.getScope().setVariable(key, v);
            programContext.getScope().setVariable(value, NumberValue.of(index++));
            try {
                body.eval();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }

    private void iterateMap(MapValue containerValue) {
        for (Map.Entry<Value, Value> entry : containerValue) {
            programContext.getScope().setVariable(key, entry.getKey());
            programContext.getScope().setVariable(value, entry.getValue());
            try {
                body.eval();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ForeachMapStatement";
    }
}
