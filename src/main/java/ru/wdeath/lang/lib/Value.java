package ru.wdeath.lang.lib;

public interface Value extends Comparable<Value> {

    Object raw();
    
    int asInt();

    double asDouble();

    String asString();

    int type();
}
