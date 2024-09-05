package ru.wdeath.lang.lib;

public class AutoCloseableScope implements AutoCloseable {
    @Override
    public void close() {
        ScopeHandler.pop();
    }
}
