package ru.wdeath.lang.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParseErrors implements Iterable<ParseError> {

    private final List<ParseError> errors;

    public ParseErrors() {
        errors = new ArrayList<>();
    }

    public void clear() {
        errors.clear();
    }

    public void add(ParseError parseError) {
        errors.add(parseError);
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
