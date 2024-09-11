package ru.wdeath.lang.lib.classes;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.Function;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.lib.Value;

import java.util.Objects;

public record ClassMethod(
        String name,
        Function function,
        ClassInstance classInstance
) implements Function {

    public ClassMethod(String name, Function function) {
        this(name, function, null);
    }

    public ClassMethod(ClassMethod m, ClassInstance instance) {
        this(m.name, m.function, instance);
    }

    @Override
    public Value execute(ProgramContext programContext, Value... args) {
        try (final var ignored = programContext.getScope().closeableScope()) {
            if (classInstance != null) {
                // non-static method
                programContext.getScope().defineVariableInCurrentScope("this", classInstance.getThisMap());
            }
            return function.execute(programContext, args);
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
