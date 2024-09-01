package ru.wdeath.lang.lib;

import ru.wdeath.lang.ast.ReturnStatement;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.exception.ArgumentsMismatchException;

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
    public Value execute(Value... values) {
        final int size = values.length;
        if (size != getArgsCount()) throw new ArgumentsMismatchException("Args count mismatch");
        try {
            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(getArgsName(i), values[i]);
            }
            body.execute();
            return NumberValue.ZERO;
        }catch (ReturnStatement rs){
            return rs.getResult();
        }finally {
            Variables.pop();
        }
    }

    @Override
    public String toString() {
        return "UserDefineFunction{" +
                "a=" + argsName +
                ", b=" + body +
                '}';
    }
}
