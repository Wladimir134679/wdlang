package ru.wdeath.lang.lib;

import ru.wdeath.lang.ast.Argument;
import ru.wdeath.lang.ast.Arguments;
import ru.wdeath.lang.ast.ReturnStatement;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.exception.ArgumentsMismatchException;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

public class UserDefinedFunction implements Function, SourceLocation {

    public final Arguments arguments;
    public final Statement body;
    private final Range range;

    public UserDefinedFunction(Arguments arguments, Statement body, Range range) {
        this.arguments = arguments;
        this.body = body;
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
    }

    public int getArgsCount() {
        return arguments.size();
    }

    public String getArgsName(int index) {
        if (index >= arguments.size() || index < 0)
            return "";
        return arguments.get(index).getName();
    }

    @Override
    public Value execute(Value[] values) {
        final int size = values.length;

        final int requiredArgsCount = arguments.getRequiredArgumentsCount();
        if (size < requiredArgsCount) {
            String error = String.format("Arguments count mismatch. %d < %d", size, requiredArgsCount);
            throw new ArgumentsMismatchException(error, arguments.getRange());
        }
        final int totalArgsCount = getArgsCount();
        if (size > totalArgsCount) {
            String error = String.format("Arguments count mismatch. %d > %d", size, totalArgsCount);
            throw new ArgumentsMismatchException(error, arguments.getRange());
        }

        try {
            ScopeHandler.push();
            for (int i = 0; i < size; i++) {
                ScopeHandler.defineVariableInCurrentScope(getArgsName(i), values[i]);
            }
            // Optional args if exists
            for (int i = size; i < totalArgsCount; i++) {
                final Argument arg = arguments.get(i);
                ScopeHandler.defineVariableInCurrentScope(arg.getName(), arg.getValueExpr().eval());
            }
            body.eval();
            return NumberValue.ZERO;
        } catch (ReturnStatement rs) {
            return rs.getResult();
        } finally {
            ScopeHandler.pop();
        }
    }

    @Override
    public String toString() {
        return arguments.toString();
    }
}
