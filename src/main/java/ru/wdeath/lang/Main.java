package ru.wdeath.lang;

import ru.wdeath.lang.ast.Expression;
import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.parser.Lexer;
import ru.wdeath.lang.parser.Parser;
import ru.wdeath.lang.parser.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        final var input = Files.readString(Path.of("./program.wdl"));
        Lexer lexer = new Lexer(input);
        List<Token> tokenize = lexer.tokenize();
        for (int i = 0; i < tokenize.size(); i++) {
            System.out.println(i + " " + tokenize.get(i));
        }
        System.out.println("======");
        final var parser = new Parser(tokenize);
        final var blockProgram = parser.parse();
        System.out.println(blockProgram);
        System.out.println("==Run==");
        blockProgram.execute();
        System.out.println("==End==");
    }
}
