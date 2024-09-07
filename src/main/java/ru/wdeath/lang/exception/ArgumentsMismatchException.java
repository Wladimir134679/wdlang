package ru.wdeath.lang.exception;

import ru.wdeath.lang.utils.Range;

public class ArgumentsMismatchException extends WdlRuntimeException{

    public ArgumentsMismatchException(String message) {
        super(message);
    }

    public ArgumentsMismatchException(String message, Range range) {
        super(message, range);
    }
}
