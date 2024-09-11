package ru.wdeath.lang.lib;

public record AutoCloseableScope(ScopeHandler scopeHandler) implements AutoCloseable {

    @Override
    public void close() {
        scopeHandler.pop();
    }
}
