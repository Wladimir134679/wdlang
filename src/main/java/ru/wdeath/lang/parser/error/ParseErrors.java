package ru.wdeath.lang.parser.error;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParseErrors extends AbstractList<ParseError> {

    private final List<ParseError> errors;

    public ParseErrors() {
        errors = new ArrayList<>();
    }

    public void clear() {
        errors.clear();
    }

    @Override
    public boolean add(ParseError parseError) {
        return errors.add(parseError);
    }

    @Override
    public ParseError get(int index) {
        return errors.get(index);
    }

    @Override
    public int size() {
        return errors.size();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public Iterator<ParseError> iterator() {
        return errors.iterator();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (ParseError error : errors) {
            result.append(error).append(System.lineSeparator());
        }
        return result.toString();
    }
}
