package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.StringValue;
import ru.wdeath.lang.lib.Value;

public class ValueExpression implements Expression {

    private final Value value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return "NE{" +
                "v=" + value +
                '}';
    }
}
