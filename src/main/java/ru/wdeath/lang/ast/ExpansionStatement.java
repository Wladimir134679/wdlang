package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.Value;
import ru.wdeath.lang.module.ExpansionModule;
import ru.wdeath.lang.module.ProgramExpansionModuleManager;
import ru.wdeath.lang.utils.Range;

import java.util.List;

public class ExpansionStatement implements Statement {

    public final ProgramContext programContext;
    public final Range range;
    public final List<String> listExpansion;

    public ExpansionStatement(ProgramContext programContext, List<String> listExpansion, Range range) {
        this.programContext = programContext;
        this.range = range;
        this.listExpansion = listExpansion;
    }

    @Override
    public Value eval() {
        for (String expansionName : listExpansion) {
            ExpansionModule expansion = ProgramExpansionModuleManager.getExpansion(expansionName);
            if (expansion == null) throw new WdlRuntimeException("Not find \"" + expansionName + "\" module", range);
            expansion.init(programContext.getScope());
        }
        return NumberValue.ZERO;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }
}
