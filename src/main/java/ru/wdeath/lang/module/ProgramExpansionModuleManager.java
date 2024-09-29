package ru.wdeath.lang.module;

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
}
