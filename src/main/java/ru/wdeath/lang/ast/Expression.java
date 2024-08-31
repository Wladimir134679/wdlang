package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public interface Expression extends Node{

    Value eval();
}
