package ru.wdeath.lang.parser.linters;

import ru.wdeath.lang.stages.impl.LinterResult;
import ru.wdeath.lang.visitors.AbstractVisitor;

import java.util.Collection;

public class LintVisitor extends AbstractVisitor {

    protected final Collection<LinterResult> results;

    public LintVisitor(Collection<LinterResult> results) {
        this.results = results;
    }
}
