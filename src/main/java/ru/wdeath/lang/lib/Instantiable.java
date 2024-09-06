package ru.wdeath.lang.lib;

/**
 * Interface for values that supports creating instances with `new` keyword.
 */
public interface Instantiable {
    
    Value newInstance(Value[] args);
}
