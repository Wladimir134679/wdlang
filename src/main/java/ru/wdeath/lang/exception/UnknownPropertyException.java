package ru.wdeath.lang.exception;

public class UnknownPropertyException extends WdlRuntimeException {

    private final String propertyName;

    public UnknownPropertyException(String name) {
        super("Unknown property " + name);
        this.propertyName = name;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
