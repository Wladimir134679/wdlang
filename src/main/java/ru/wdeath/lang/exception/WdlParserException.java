package ru.wdeath.lang.exception;

import ru.wdeath.lang.parser.error.ParseError;
import ru.wdeath.lang.parser.error.ParseErrors;
import ru.wdeath.lang.stages.util.SourceLocatedError;

import java.util.Collection;
import java.util.List;

public class WdlParserException extends RuntimeException{

    private final Collection<? extends SourceLocatedError> errors;

    public WdlParserException(SourceLocatedError error) {
        super(error.toString());
        errors = List.of(error);;
    }

    public WdlParserException(Collection<? extends SourceLocatedError> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public Collection<? extends SourceLocatedError> getParseErrors() {
        return errors;
    }
}
