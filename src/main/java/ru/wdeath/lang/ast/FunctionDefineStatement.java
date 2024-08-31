package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.UserDefineFunction;

import java.util.List;

public class FunctionDefineStatement implements Statement{

    private final String name;
    private final List<String> argsName;
    private final Statement body;

    public FunctionDefineStatement(String name, List<String> argsName, Statement body) {
        this.name = name;
        this.argsName = argsName;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.addFunction(name, new UserDefineFunction(argsName, body));
    }

    @Override
    public String toString() {
        return "FuncDefS{" +
                "n='" + name + '\'' +
                ", a=" + argsName +
                ", b=" + body +
                '}';
    }
}
