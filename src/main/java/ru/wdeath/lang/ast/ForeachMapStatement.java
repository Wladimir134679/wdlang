package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

import java.util.Map;

public class ForeachMapStatement implements Statement {

    public final String key, value;
    public final Expression container;
    public final Statement body;

    public ForeachMapStatement(String key, String value, Expression container, Statement body) {
        this.key = key;
        this.value = value;
        this.container = container;
        this.body = body;
    }

    @Override
    public void execute() {
        final Value previousVariableValue1 = Variables.isExists(key) ? Variables.get(key) : null;
        final Value previousVariableValue2 = Variables.isExists(value) ? Variables.get(value) : null;
        final Iterable<Map.Entry<Value, Value>> iterator = (Iterable<Map.Entry<Value, Value>>) container.eval();
        for (Map.Entry<Value, Value> entry : iterator) {
            Variables.set(key, entry.getKey());
            Variables.set(value, entry.getValue());
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
        // Восстанавливаем переменные
        if (previousVariableValue1 != null) {
            Variables.set(key, previousVariableValue1);
        }
        if (previousVariableValue2 != null) {
            Variables.set(value, previousVariableValue2);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
