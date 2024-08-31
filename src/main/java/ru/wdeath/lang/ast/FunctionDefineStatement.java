package ru.wdeath.lang.ast;

import ru.wdeath.lang.lib.Functions;
import ru.wdeath.lang.lib.UserDefineFunction;

import java.util.List;

public class FunctionDefineStatement implements Statement{

    public final String name;
    public final List<String> argsName;
    public final Statement body;

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
    public void accept(Visitor visitor) {
        visitor.visit(this);
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
