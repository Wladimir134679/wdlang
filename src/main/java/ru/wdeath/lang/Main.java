package ru.wdeath.lang;

import ru.wdeath.lang.ast.ExprStatement;
import ru.wdeath.lang.lib.CallStack;
import ru.wdeath.lang.parser.Lexer;
import ru.wdeath.lang.parser.Parser;
import ru.wdeath.lang.parser.Token;
import ru.wdeath.lang.visitors.FunctionAdder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            final var input = Files.readString(Path.of("./program1.wdl"));
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
            blockProgram.accept(new FunctionAdder());
            blockProgram.execute();
            System.out.println("==End==");
        }catch (Exception ex){
            handleException(Thread.currentThread(), ex);
        }
    }

    public static void handleException(Thread thread, Throwable throwable) {
        System.err.printf("%s in %s\n", throwable.getMessage(), thread.getName());
        for (CallStack.CallInfo call : CallStack.getCalls()) {
            System.err.printf("\tat %s\n", call);
        }
//        throwable.printStackTrace();
    }
}
