package com.wdeath.wdlang.script.parser.optimization;


import com.wdeath.wdlang.script.parser.ast.Node;

public interface Optimizable {

    Node optimize(Node node);

    int optimizationsCount();

    String summaryInfo();
}
