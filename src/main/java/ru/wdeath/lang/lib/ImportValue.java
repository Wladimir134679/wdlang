package ru.wdeath.lang.lib;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.exception.WdlRuntimeException;

import javax.lang.model.type.UnknownTypeException;

public class ImportValue implements Value {

    private final ProgramContext context;

    public ImportValue(ProgramContext context) {
        this.context = context;
    }

    @Override
    public Object raw() {
        return context;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast importValue to integer");
    }

    @Override
    public double asDouble() {
        throw new TypeException("Cannot cast importValue to double");
    }

    @Override
    public String asString() {
        return "importValue = " + context.toString();
    }

    @Override
    public int type() {
        return Types.IMPORT;
    }

    @Override
    public int compareTo(Value o) {
        throw new TypeException("Not implemented yet");
    }

    @Override
    public String toString() {
        return asString();
    }

    public Value access(Value lastIndex) {
        Value variableOrConstant = context.getScope().getVariableOrConstant(lastIndex.asString());
        return variableOrConstant;
    }
}
