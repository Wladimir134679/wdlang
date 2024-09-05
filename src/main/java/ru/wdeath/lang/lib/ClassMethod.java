package ru.wdeath.lang.lib;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.ast.Arguments;

public class ClassMethod extends UserDefinedFunction {

    public final ClassInstanceValue classInstance;

    public ClassMethod(Arguments arguments, Statement body, ClassInstanceValue classInstance) {
        super(arguments, body);
        this.classInstance = classInstance;
    }

    @Override
    public Value execute(Value[] values) {
        ScopeHandler.push();
        ScopeHandler.defineVariableInCurrentScope("this", classInstance.getThisMap());

        try {
            return super.execute(values);
        } finally {
            ScopeHandler.pop();
        }
    }
}
