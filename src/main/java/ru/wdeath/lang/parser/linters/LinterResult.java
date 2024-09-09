package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.stages.util.SourceLocatedError;
import ru.wdeath.lang.utils.Range;

public record LinterResult(Severity severity, String message, Range range)
        implements SourceLocatedError {

    public enum Severity {ERROR, WARNING}

    LinterResult(Severity severity, String message) {
        this(severity, message, null);
    }

    public static LinterResult warning(String message) {
        return new LinterResult(Severity.WARNING, message);
    }

    public static LinterResult warning(String message, Range range) {
        return new LinterResult(Severity.WARNING, message, range);
    }


    public static LinterResult error(String message) {
        return new LinterResult(Severity.ERROR, message);
    }

    public static LinterResult error(String message, Range range) {
        return new LinterResult(Severity.ERROR, message, range);
    }


    public boolean isError() {
        return severity == Severity.ERROR;
    }

    public boolean isWarning() {
        return severity == Severity.WARNING;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public String toString() {
        return severity.name() + ": " + message;
    }
}
