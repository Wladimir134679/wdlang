package ru.wdeath.lang.module;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.lib.ImportValue;
import ru.wdeath.lang.module.impl.MathModule;
import ru.wdeath.lang.module.impl.STDModule;

import java.util.HashMap;

public class ProgramExpansionModuleManager {

    private static final HashMap<String, ExpansionModule> modules = new HashMap<>();

    static {
        modules.put("std", new STDModule());
        modules.put("math", new MathModule());
    }

    public static ExpansionModule getExpansion(String name) {
        return modules.get(name);
    }

    public static boolean isExists(String name) {
        return modules.containsKey(name);
    }

    public static ProgramContext expansion(String name, ProgramContext currentContext) {
        if (!isExists(name)) throw new WdlRuntimeException("Not find \"" + name + "\" module");
        ExpansionModule expansion = getExpansion(name);
        ProgramContext programContext = new ProgramContext("import " + expansion.getClass().getName());
        programContext.setConsole(currentContext.getConsole());
        expansion.init(programContext, programContext.getScope());
        return programContext;
    }
}
