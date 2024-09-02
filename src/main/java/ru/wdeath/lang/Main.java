package ru.wdeath.lang;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.lib.CallStack;
import ru.wdeath.lang.parser.Lexer;
import ru.wdeath.lang.parser.Optimizer;
import ru.wdeath.lang.parser.Parser;
import ru.wdeath.lang.parser.Token;
import ru.wdeath.lang.utils.TimeMeasurement;
import ru.wdeath.lang.visitors.FunctionAdder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            final var input = Files.readString(Path.of("./program1.wdl"));
            final TimeMeasurement measurement = new TimeMeasurement();
            measurement.start("Tokenize time");
            List<Token> tokenize = Lexer.tokenize(input);
            measurement.stop("Tokenize time");
            for (int i = 0; i < tokenize.size(); i++) {
                System.out.println(i + " " + tokenize.get(i));
            }
            System.out.println("======");

            measurement.start("Parse time");
            final var parser = new Parser(tokenize);
            final var blockProgram = parser.parse();
            measurement.stop("Parse time");

            if (parser.getParseErrors().hasErrors()) {
                System.out.println(parser.getParseErrors());
                return;
            }
            System.out.println(blockProgram);

            measurement.start("Optimization time");
            Statement program = Optimizer.optimize(blockProgram, 9);
            measurement.stop("Optimization time");

            program.accept(new FunctionAdder());

            measurement.start("Execution time");
            System.out.println("==Run==");
            program.execute();
            System.out.println("==End==");
            measurement.stop("Execution time");
            System.out.println(measurement.summary(TimeUnit.MILLISECONDS, true));
        }catch (Exception ex){
            handleException(Thread.currentThread(), ex);
        }
    }

    public static void handleException(Thread thread, Throwable throwable) {
        System.err.printf("%s in %s\n", throwable.getMessage(), thread.getName());
        for (CallStack.CallInfo call : CallStack.getCalls()) {
            System.err.printf("\tat %s\n", call);
        }
        throwable.printStackTrace();
    }
}
