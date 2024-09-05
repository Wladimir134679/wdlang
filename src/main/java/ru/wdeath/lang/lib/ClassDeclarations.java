package ru.wdeath.lang.lib;

import ru.wdeath.lang.ast.ClassDeclarationStatement;
import ru.wdeath.lang.exception.UnknownFunctionException;

import java.util.HashMap;
import java.util.Map;

public class ClassDeclarations {

    private static final Map<String, ClassDeclarationStatement> declarations;
    static {
        declarations = new HashMap<>();
    }

    private ClassDeclarations() { }

    public static void clear() {
        declarations.clear();
    }

    public static Map<String, ClassDeclarationStatement> getAll() {
        return declarations;
    }

    public static boolean isExists(String key) {
        return declarations.containsKey(key);
    }

    public static ClassDeclarationStatement get(String key) {
        if (!isExists(key)) throw new UnknownFunctionException(key);
        return declarations.get(key);
    }

    public static void set(String key, ClassDeclarationStatement classDef) {
        declarations.put(key, classDef);
    }
}
