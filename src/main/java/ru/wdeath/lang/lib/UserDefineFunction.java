package ru.wdeath.lang.lib;

import ru.wdeath.lang.ast.ReturnStatement;
import ru.wdeath.lang.ast.Statement;

import java.util.List;

public class UserDefineFunction implements Function{

    private final List<String> argsName;
    private final Statement body;

    public UserDefineFunction(List<String> argsName, Statement body) {
        this.argsName = argsName;
        this.body = body;
    }

    public int getArgsCount() {
        return argsName.size();
    }

    public String getArgsName(int index) {
        if(index >= argsName.size() || index < 0)
            return "";
        return argsName.get(index);
    }

    @Override
    public Value execute(Value... v) {
        try {
            body.execute();
            return NumberValue.ZERO;
        }catch (ReturnStatement rs){
            return rs.getResult();
        }
    }
}
