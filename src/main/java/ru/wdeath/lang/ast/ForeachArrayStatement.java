package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.lib.*;

import java.util.Map;

public class ForeachArrayStatement extends InterruptableNode implements Statement {

    public final ProgramContext programContext;
    public final String variable;
    public final Node container;
    public final Statement body;

    public ForeachArrayStatement(ProgramContext programContext, String variable, Node container, Statement body) {
        this.programContext = programContext;
        this.variable = variable;
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
                default -> throw new TypeException("Cannot iterate " + Types.typeToString(containerValue.type()));
            }
        }
        return NumberValue.ZERO;
    }

    private void iterateString(String str) {
        for (char ch : str.toCharArray()) {
            programContext.getScope().setVariable(variable, new StringValue(String.valueOf(ch)));
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
        for (Value value : containerValue) {
            programContext.getScope().setVariable(variable, value);
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
            programContext.getScope().setVariable(variable, new ArrayValue(new Value[]{
                    entry.getKey(),
                    entry.getValue()
            }));
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
        return "ForeachArrayStatement";
    }
}
