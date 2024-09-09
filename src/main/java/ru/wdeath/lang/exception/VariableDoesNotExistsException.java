package ru.wdeath.lang.exception;

import ru.wdeath.lang.utils.Range;

public class VariableDoesNotExistsException extends WdlRuntimeException {

    private final String variable;

    public VariableDoesNotExistsException(String variable, Range range) {
        super("Variable " + variable + " does not exist", range);
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }
}
