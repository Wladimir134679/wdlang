package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.parser.Parser;
import ru.wdeath.lang.parser.Token;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.util.List;

public class ParserStage implements Stage<List<Token>, Statement> {

    public static final String TAG_PROGRAM = "program";
    public static final String TAG_HAS_PARSE_ERRORS = "hasParseErrors";

    @Override
    public Statement perform(StagesData stagesData, List<Token> input) {
        final Parser parser = new Parser(input);
        final Statement program = parser.parse();
        final var parseErrors = parser.getParseErrors();
        stagesData.put(TAG_PROGRAM, program);
        stagesData.put(TAG_HAS_PARSE_ERRORS, parseErrors.hasErrors());
        if (parseErrors.hasErrors()) {
            throw new WdlParserException(parseErrors);
        }
        return program;
    }
}
