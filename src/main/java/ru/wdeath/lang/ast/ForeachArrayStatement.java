package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.lib.Variables;

public class ForeachArrayStatement  implements Statement {

    public final String variable;
    public final Expression container;
    public final Statement body;

    public ForeachArrayStatement(String variable, Expression container, Statement body) {
        this.variable = variable;
        this.container = container;
        this.body = body;
    }

    @Override
    public void execute() {
        final Value previousVariableValue = Variables.isExists(variable) ? Variables.get(variable) : null;
        final Iterable<Value> iterator = (Iterable<Value>) container.eval();
        for (Value value : iterator) {
            Variables.set(variable, value);
            try {
                body.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
        // Восстанавливаем переменную
        if (previousVariableValue != null) {
            Variables.set(variable, previousVariableValue);
        }
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
