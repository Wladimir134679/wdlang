package ru.wdeath.lang;

import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.utils.Console;

public class ProgramContext {

    private final ScopeHandler scopeHandler;
    private final Console console;

    public ProgramContext(){
        console = new Console();
        scopeHandler = new ScopeHandler();
    }

    public void reset(){
        scopeHandler.resetScope();
        Functions.clearAndInit(this);
    }

    public ScopeHandler getScope() {
        return scopeHandler;
    }

    public Console getConsole() {
        return console;
    }
}
