package ru.wdeath.lang.lib;

public interface Value {

    Object raw();
    
    int asInt();

    double asDouble();

    String asString();

    int type();
}
