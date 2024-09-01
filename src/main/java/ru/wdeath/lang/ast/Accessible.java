package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public interface Accessible {

    Value get();

    Value set(Value value);
}
