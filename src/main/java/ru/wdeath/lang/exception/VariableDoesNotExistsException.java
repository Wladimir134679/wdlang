package ru.wdeath.lang.exception;

public class VariableDoesNotExistsException extends RuntimeException {

    private final String variable;

    public VariableDoesNotExistsException(String variable) {
        super("Variable " + variable + " does not exist");
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}