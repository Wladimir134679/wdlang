package ru.wdeath.lang.lib;

import ru.wdeath.lang.ProgramContext;

public class ProgramLibFunction implements Function {

    private final ProgramContext context;
    private final FuncProgram funcProgram;

    public ProgramLibFunction(ProgramContext context, FuncProgram funcProgram) {
        this.context = context;
        this.funcProgram = funcProgram;
    }

    public ProgramLibFunction(FuncProgram funcProgram) {
        this.context = ProgramContext.GLOBAL;
        this.funcProgram = funcProgram;
    }


    @Override
    public Value execute(Value... v) {
        return funcProgram.execute(context, v);
    }

    public interface FuncProgram {
        Value execute(ProgramContext pc, Value... v);
    }

    @Override
    public String toString() {
        return "libFunc { contextHash=" + context.hashCode() + ", func=" + funcProgram + '}';
    }
}
