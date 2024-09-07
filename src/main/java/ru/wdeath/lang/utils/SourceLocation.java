package ru.wdeath.lang.utils;

public interface SourceLocation {

    default Range getRange() {
        return null;
    }
}
