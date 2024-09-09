package ru.wdeath.lang.lib.classes;

import ru.wdeath.lang.ast.ClassDeclarationStatement;
import ru.wdeath.lang.exception.UnknownFunctionException;
import ru.wdeath.lang.lib.Instantiable;
import ru.wdeath.lang.lib.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ClassDeclaration(
        String name,
        List<ClassField> classFields,
        List<ClassMethod> classMethods
) implements Instantiable {


    @Override
    public Value newInstance(Value[] args) {
        final var instance = new ClassInstance(name);
        for (ClassField f : classFields) {
            instance.addField(f);
        }
        for (ClassMethod m : classMethods) {
            instance.addMethod(m);
        }
        return instance.callConstructor(args);
    }
}
