package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.*;

public class ValueExpression implements Expression {

    public final Value value;

    public ValueExpression(Function value) {
        this.value = new FunctionValue(value);
    }

    public ValueExpression(Value value) {
        this.value = value;
    }

    public ValueExpression(Number value) {
        this.value = NumberValue.of(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        if (value.type() == Types.STRING) {
            return "\"" + value.asString() + "\"";
        }
        return value.toString();
    }
}
