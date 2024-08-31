package ru.wdeath.lang.ast;

public interface Node {

    void accept(Visitor visitor);
}
