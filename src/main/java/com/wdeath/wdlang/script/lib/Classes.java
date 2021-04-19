package com.wdeath.wdlang.script.lib;

import com.wdeath.wdlang.script.exceptions.*;
import java.util.HashMap;
import java.util.Map;

public final class Classes {

    private final Map<String, ClassInstanceValue> classes;

    public Classes() {
        classes = new HashMap<>();
    }

    public void clear() {
        classes.clear();
    }

    public Map<String, ClassInstanceValue> getFunctions() {
        return classes;
    }
    
    public boolean isExists(String key) {
        return classes.containsKey(key);
    }
    
    public ClassInstanceValue get(String key) {
        if (!isExists(key)) throw new UnknownClassException(key);
        return classes.get(key);
    }
    
    public void set(String key, ClassInstanceValue classDef) {
        classes.put(key, classDef);
    }
}
