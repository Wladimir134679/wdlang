package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public interface Accessible extends Node {

    Value get();

    Value set(Value value);
}
