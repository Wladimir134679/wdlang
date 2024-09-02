package ru.wdeath.lang.visitors.optimization;

import ru.wdeath.lang.lib.Value;

public class VariableInfo {

    public Value value;
    public int modifications;

    @Override
    public String toString() {
        return (value == null ? "?" : value) + " (" + modifications + " mods)";
    }
}
