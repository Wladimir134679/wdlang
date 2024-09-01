package ru.wdeath.lang.exception;

public class UnknownFunctionException extends RuntimeException{

    private final String functionName;

    public UnknownFunctionException(String functionName) {
        super("Unknown function: " + functionName);
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }
}
