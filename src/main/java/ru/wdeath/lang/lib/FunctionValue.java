package ru.wdeath.lang.lib;

public class FunctionValue implements Value{

    private final Function function;

    public FunctionValue(Function function) {
        this.function = function;
    }

    @Override
    public double asDouble() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public String asString() {
        return function.toString();
    }

    public Function getFunction() {
        return function;
    }
}
