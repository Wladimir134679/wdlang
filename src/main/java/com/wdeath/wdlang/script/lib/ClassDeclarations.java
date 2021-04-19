package com.wdeath.wdlang.script.lib;

import com.wdeath.wdlang.script.parser.ast.ClassDeclarationStatement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ClassDeclarations {

    private final Map<String, ClassDeclarationStatement> declarations;

    public ClassDeclarations(){
        declarations = new ConcurrentHashMap<>();
    }

    public void clear() {
        declarations.clear();
    }

    public Map<String, ClassDeclarationStatement> getAll() {
        return declarations;
    }
    
    public ClassDeclarationStatement get(String key) {
        return declarations.get(key);
    }
    
    public void set(String key, ClassDeclarationStatement classDef) {
        declarations.put(key, classDef);
    }
}
