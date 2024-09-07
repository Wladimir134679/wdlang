package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.utils.SourceLocation;

public interface SourceLocatedError extends SourceLocation {

    String getMessage();

    default StackTraceElement[] getStackTrace() {
        return new StackTraceElement[0];
    }

    default boolean hasStackTrace() {
        return !stackTraceIsEmpty();
    }

    private boolean stackTraceIsEmpty() {
        final var st = getStackTrace();
        return st == null || st.length == 0;
    }
}
