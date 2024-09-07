package ru.wdeath.lang.ast;

public record Argument(String name, Node valueExpr) {

    public Argument(String name) {
        this(name, null);
    }


    public String getName() {
        return name;
    }

    public Node getValueExpr() {
        return valueExpr;
    }

    @Override
    public String toString() {
        return name + (valueExpr == null ? "" : " = " + valueExpr);
    }
}
