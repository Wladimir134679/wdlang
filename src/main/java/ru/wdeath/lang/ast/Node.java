package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Value;

public interface Node {

    Value eval();

    void accept(Visitor visitor);

    <R, T> R accept(ResultVisitor<R, T> visitor, T input);
}
