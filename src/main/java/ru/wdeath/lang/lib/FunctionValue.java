package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;

import java.util.Objects;

public class FunctionValue implements Value{

    private final Function function;

    public FunctionValue(Function function) {
        this.function = function;
    }

    @Override
    public double asDouble() {
        throw new TypeException("Not implemented");
    }

    @Override
    public String asString() {
        return function.toString();
    }

    public Function getFunction() {
        return function;
    }

    @Override
    public int type() {
        return Types.FUNCTION;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.function);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final FunctionValue other = (FunctionValue) obj;
        return Objects.equals(this.function, other.function);
    }
}
