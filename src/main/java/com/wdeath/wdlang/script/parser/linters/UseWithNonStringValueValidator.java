package com.wdeath.wdlang.script.parser.linters;


import com.wdeath.wdlang.script.lib.Types;
import com.wdeath.wdlang.script.lib.Value;
import com.wdeath.wdlang.script.parser.ast.*;

public final class UseWithNonStringValueValidator extends LintVisitor {

    @Override
    public void visit(IncludeStatement st) {
        super.visit(st);
        applyVisitor(st, this);
    }

    @Override
    public void visit(UseStatement st) {
        super.visit(st);

        if (st.expression instanceof ArrayExpression) {
            ArrayExpression ae = (ArrayExpression) st.expression;
            for (Expression expr : ae.elements) {
                if (!checkExpression(expr)) {
                    return;
                }
            }
        } else {
            if (!checkExpression(st.expression)) {
                return;
            }
        }
    }

    private boolean checkExpression(Expression expr) {
        if (!(expr instanceof ValueExpression)) {
            System.err.println(String.format(
                    "Warning: `use` with %s, not ValueExpression", expr.getClass().getSimpleName()));
            return false;
        }

        final Value value = ((ValueExpression) expr).value;
        if (value.type() != Types.STRING) {
            warnWrongType(value);
            return false;
        }
        return true;
    }

    private void warnWrongType(Value value) {
        System.err.println(
                String.format(
                        "Warning: `use` with %s - %s, not string",
                        Types.typeToString(value.type()), value.asString())
        );
    }
}
