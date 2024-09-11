package ru.wdeath.lang.lib;

import ru.wdeath.lang.ast.Visitor;

public interface Value extends Comparable<Value> {

    Object raw();
    
    int asInt();

    double asDouble();

    String asString();

    int type();

    default Object asJavaObject() {
        return raw();
    }
}
