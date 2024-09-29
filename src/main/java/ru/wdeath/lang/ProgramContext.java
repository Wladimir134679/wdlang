package ru.wdeath.lang;

import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.utils.Console;

public class ProgramContext {

    public static ProgramContext GLOBAL = null;

    private final String name;
    private final ScopeHandler scopeHandler;
    private Console console;

    public ProgramContext(String name){
        this.name = name;
        if(GLOBAL == null){
            GLOBAL = this;
        }
        console = new Console();
        scopeHandler = new ScopeHandler();
    }

    public void reset(){
        scopeHandler.resetScope();
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

    public String getName() {
        return name;
    }
}
