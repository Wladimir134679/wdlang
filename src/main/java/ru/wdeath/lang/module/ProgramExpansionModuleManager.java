package ru.wdeath.lang.module;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.lib.ImportValue;
import ru.wdeath.lang.module.impl.MathModule;
import ru.wdeath.lang.module.impl.STDModule;

import java.util.HashMap;

public class ProgramExpansionModuleManager {

    private final HashMap<String, ExpansionModule> modules = new HashMap<>();

    public ProgramExpansionModuleManager() {
        this
                .add(new STDModule())
                .add(new MathModule());
    }

    public ProgramExpansionModuleManager add(String name, ExpansionModule module) {
        modules.put(name, module);
        return this;
    }

    public ProgramExpansionModuleManager add(NameExpansionModule module) {
        modules.put(module.name(), module);
        return this;
    }

    public ExpansionModule getExpansion(String name) {
        return modules.get(name);
    }

    public boolean isExists(String name) {
        return modules.containsKey(name);
    }

    public ProgramContext expansion(String name, ProgramContext currentContext) {
        if (!isExists(name)) throw new WdlRuntimeException("Not find \"" + name + "\" module");
        ExpansionModule expansion = getExpansion(name);
        InitModule initModule = new InitModule(createProgramContextModule(currentContext, expansion));
        expansion.init(initModule);
        return initModule.programContext();
    }

    private ProgramContext createProgramContextModule(ProgramContext currentContext, ExpansionModule expansion) {
        return new ProgramContext("import " + expansion.getClass().getName(), currentContext);
    }
}
