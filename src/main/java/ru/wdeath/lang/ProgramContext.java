package ru.wdeath.lang;

import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.utils.Console;

public class ProgramContext {

    public static ProgramContext GLOBAL = null;

    private final ScopeHandler scopeHandler;
    private Console console;

    public ProgramContext(){
        if(GLOBAL == null){
            GLOBAL = this;
        }
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

    public void setConsole(Console console) {
        this.console = console;
    }
}
