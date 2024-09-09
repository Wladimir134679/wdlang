package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.visitors.AbstractVisitor;

import java.util.Collection;

public class LintVisitor extends AbstractVisitor {

    protected final LinterResults results;

    public LintVisitor(LinterResults  results) {
        this.results = results;
    }
}
