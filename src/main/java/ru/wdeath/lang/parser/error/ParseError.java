package ru.wdeath.lang.parser.error;

import ru.wdeath.lang.stages.util.SourceLocatedError;
import ru.wdeath.lang.utils.Range;

import java.util.Collections;
import java.util.List;

public record ParseError(String message, Range range, List<StackTraceElement> stackTraceElements)
        implements SourceLocatedError {

    public ParseError(String message, Range range) {
        this(message, range, Collections.emptyList());
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return stackTraceElements.toArray(new StackTraceElement[0]);
    }

    @Override
    public boolean hasStackTrace() {
        return !stackTraceElements.isEmpty();
    }

    @Override
    public String toString() {
        return "Error on line " + range().start().row() + ": " + message;
    }
}
