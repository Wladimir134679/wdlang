package ru.wdeath.lang.lib;

import ru.wdeath.lang.ProgramContext;

/**
 * Interface for values that supports creating instances with `new` keyword.
 */
public interface Instantiable {

    Value newInstance(Value[] args, ProgramContext programContext);
}
