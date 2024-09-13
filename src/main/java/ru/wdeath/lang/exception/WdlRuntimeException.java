package ru.wdeath.lang.exception;

import ru.wdeath.lang.stages.util.SourceLocatedError;
import ru.wdeath.lang.utils.Range;

public class WdlRuntimeException extends RuntimeException implements SourceLocatedError {

    private final Range range;

    public WdlRuntimeException() {
        super();
        this.range = null;
    }

    public WdlRuntimeException(Exception ex) {
        super(ex);
        this.range = null;
    }

    public WdlRuntimeException(String message) {
        this(message, (Range) null);
    }

    public WdlRuntimeException(String message, Range range) {
        super(message);
        this.range = range;
    }


    public WdlRuntimeException(String message, Throwable ex) {
        this(message, ex, null);
    }


    public WdlRuntimeException(String message, Throwable ex, Range range) {
        super(message, ex);
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }
}
