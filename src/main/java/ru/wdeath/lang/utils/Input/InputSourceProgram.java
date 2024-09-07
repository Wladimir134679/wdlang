package ru.wdeath.lang.utils.Input;

public record InputSourceProgram(String program) implements InputSource {

    @Override
    public String getPath() {
        return ".";
    }

    @Override
    public String load() {
        return program;
    }

    @Override
    public String toString() {
        return "Program";
    }
}
