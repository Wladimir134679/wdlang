package ru.wdeath.lang;

import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.module.ProgramExpansionModuleManager;
import ru.wdeath.lang.utils.Console;

public class ProgramContext {

    public static ProgramContext GLOBAL = null;

    private final String name;
    private final ScopeHandler scopeHandler;
    private final ProgramExpansionModuleManager moduleManager;
    private Console console;

    public ProgramContext(String name){
        this.name = name;
        if(GLOBAL == null){
            GLOBAL = this;
        }
        moduleManager = new ProgramExpansionModuleManager();
        console = new Console();
        scopeHandler = new ScopeHandler();
    }

    public ProgramContext(String name, ProgramContext context){
        this.name = name;
        if(GLOBAL == null){
            GLOBAL = this;
        }
        moduleManager = context.getModuleManager();
        console = context.getConsole();
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

    public ProgramExpansionModuleManager getModuleManager() {
        return moduleManager;
    }

}
