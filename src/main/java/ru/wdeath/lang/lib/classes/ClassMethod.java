package ru.wdeath.lang.lib.classes;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.Function;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.lib.Value;

import java.util.Objects;

public record ClassMethod(
        ProgramContext programContext,
        String name,
        Function function,
        ClassInstance classInstance
) implements Function {

    public ClassMethod(ProgramContext programContext, String name, Function function) {
        this(programContext, name, function, null);
    }

    public ClassMethod(ProgramContext programContext, ClassMethod m, ClassInstance instance) {
        this(programContext, m.name, m.function, instance);
    }

    @Override
    public Value execute(Value... args) {
        try (final var ignored = programContext.getScope().closeableScope()) {
            if (classInstance != null) {
                // non-static method
                programContext.getScope().defineVariableInCurrentScope("this", classInstance.getThisMap());
            }
            return function.execute(args);
        }
    }

    @Override
    public int getArgsCount() {
        return function.getArgsCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassMethod that)) return false;
        return Objects.equals(name, that.name)
                && Objects.equals(function, that.function);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, function);
    }

    @Override
    public String toString() {
        return "ClassMethod[" + name + ']';
    }
}
