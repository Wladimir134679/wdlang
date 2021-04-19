package com.wdeath.wdlang.script.lib;

import com.wdeath.wdlang.script.exceptions.*;
import java.util.HashMap;
import java.util.Map;

public final class Functions {

    private final Map<String, Function> functions;

    public Functions() {
        functions = new HashMap<>();
    }

    public void clear() {
        functions.clear();
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }
    
    public boolean isExists(String key) {
        return functions.containsKey(key);
    }
    
    public Function get(String key) {
        if (!isExists(key)) throw new UnknownFunctionException(key);
        return functions.get(key);
    }
    
    public void set(String key, Function function) {
        functions.put(key, function);
    }
}
