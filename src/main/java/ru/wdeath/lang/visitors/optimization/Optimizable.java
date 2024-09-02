package ru.wdeath.lang.visitors.optimization;

import ru.wdeath.lang.ast.Node;

public interface Optimizable {

    Node optimize(Node node);

    int optimizationsCount();

    String summaryInfo();
}
