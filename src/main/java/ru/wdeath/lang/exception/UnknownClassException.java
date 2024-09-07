package ru.wdeath.lang.exception;


import ru.wdeath.lang.utils.Range;

public final class UnknownClassException extends WdlRuntimeException {

    private final String className;

    public UnknownClassException(String name, Range range) {
        super("Unknown class " + name, range);
        this.className = name;
    }

    public String getClassName() {
        return className;
    }
}
