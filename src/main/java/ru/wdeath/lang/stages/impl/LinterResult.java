package ru.wdeath.lang.stages.impl;

public record LinterResult(Severity severity, String message) {

    public enum Severity { ERROR, WARNING }

    @Override
    public String toString() {
        return severity.name() + ": " + message;
    }
}
