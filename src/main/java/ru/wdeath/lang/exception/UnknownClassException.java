package ru.wdeath.lang.exception;


public final class UnknownClassException extends WdlRuntimeException {

    private final String className;

    public UnknownClassException(String name) {
        super("Unknown class " + name);
        this.className = name;
    }

    public String getClassName() {
        return className;
    }
}
