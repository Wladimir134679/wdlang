package ru.wdeath.lang.lib;

import ru.wdeath.lang.ProgramContext;

public interface Function {

    Value execute(Value... v);

    default int getArgsCount() {
        return 0;
    }
}
