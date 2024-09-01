package ru.wdeath.lang.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arguments implements Iterable<Argument> {

    private final List<Argument> arguments;
    private int requiredArgumentsCount;

    public Arguments() {
        arguments = new ArrayList<>();
        requiredArgumentsCount = 0;
    }

    public void addRequired(String name) {
        arguments.add(new Argument(name));
        requiredArgumentsCount++;
    }

    public void addOptional(String name, Expression expr) {
        arguments.add(new Argument(name, expr));
    }

    public Argument get(int index) {
        return arguments.get(index);
    }

    public int getRequiredArgumentsCount() {
        return requiredArgumentsCount;
    }

    public int size() {
        return arguments.size();
    }

    @Override
    public Iterator<Argument> iterator() {
        return arguments.iterator();
    }
}
