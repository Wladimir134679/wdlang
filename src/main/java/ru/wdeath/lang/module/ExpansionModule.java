package ru.wdeath.lang.module;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.Scope;
import ru.wdeath.lang.lib.ScopeHandler;

public interface ExpansionModule {

    void init(ProgramContext context, ScopeHandler scope);
}
