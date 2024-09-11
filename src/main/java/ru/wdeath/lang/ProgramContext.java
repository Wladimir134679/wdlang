package ru.wdeath.lang;

import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.ScopeHandler;

public class ProgramContext {

    private ScopeHandler scopeHandler;

    public ProgramContext(){
        scopeHandler = new ScopeHandler();
    }

    public void reset(){
        scopeHandler.resetScope();
        Functions.clearAndInit(this);
    }

    public ScopeHandler getScope() {
        return scopeHandler;
    }
}
