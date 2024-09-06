package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.parser.Lexer;
import ru.wdeath.lang.parser.Token;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.util.List;

public class LexerStage implements Stage<String, List<Token>> {

    public static final String TAG_TOKENS = "tokens";

    @Override
    public List<Token> perform(StagesData stagesData, String input) {
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        stagesData.put(TAG_TOKENS, tokens);
        return tokens;
    }
}
