package ru.wdeath.lang.exception;

public class OperationIsNotSupportedException extends RuntimeException{

    private final Object operator;

    public OperationIsNotSupportedException(Object operator) {
        super("Operator " + operator + " is not supported");
        this.operator = operator;
    }

    public Object getOperator() {
        return operator;
    }
}
