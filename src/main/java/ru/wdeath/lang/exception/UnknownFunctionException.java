package ru.wdeath.lang.exception;

import ru.wdeath.lang.utils.Range;

public class UnknownFunctionException extends WdlRuntimeException{

    private final String functionName;

    public UnknownFunctionException(String name) {
        super("Unknown function " + name);
        this.functionName = name;
    }

    public UnknownFunctionException(String name, Range range) {
        super("Unknown function " + name, range);
        this.functionName = name;
    }

    public String getFunctionName() {
        return functionName;
    }
}
