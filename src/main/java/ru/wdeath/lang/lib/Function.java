package ru.wdeath.lang.lib;

public interface Function {

    Value execute(Value... v);

    default int getArgsCount() {
        return 0;
    }
}
