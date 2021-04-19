package com.wdeath.wdlang.script.parser.optimization;


import com.wdeath.wdlang.script.lib.Value;

public final class VariableInfo {
    public Value value;
    int modifications;

    @Override
    public String toString() {
        return (value == null ? "?" : value) + " (" + modifications + " mods)";
    }
}