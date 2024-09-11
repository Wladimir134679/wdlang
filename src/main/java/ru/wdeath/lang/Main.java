package ru.wdeath.lang;

import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.parser.linters.LinterStage;
import ru.wdeath.lang.parser.opttimization.OptimizationStage;
import ru.wdeath.lang.stages.ScopedStageFactory;
import ru.wdeath.lang.stages.StagesDataMap;
import ru.wdeath.lang.stages.impl.*;
import ru.wdeath.lang.utils.Console;
import ru.wdeath.lang.utils.Input.InputSourceFile;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.TimeMeasurement;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final boolean GENERATE_MERMAID_TEXT = true;

    public static void main(String[] args) throws IOException {
        final var measurement = new TimeMeasurement();
        final var scopedStages = new ScopedStageFactory(measurement::start, measurement::stop);
        final var input = new InputSourceFile("./examples/testTree_fun.wdl");
        final var stagesData = new StagesDataMap();
        final var programContext = new ProgramContext();
        try {
            stagesData.put(SourceLoaderStage.TAG_SOURCE_LINES, input);

            scopedStages.create("Load source", new SourceLoaderStage())
                    .then(scopedStages
                            .create("Lexer", new LexerStage()))
                    .then(scopedStages
                            .create("Parser", new ParserStage()))
                    .thenConditional(true, scopedStages
                            .create("Optimization", new OptimizationStage(10, true)))
                    .then(scopedStages
                            .create("Linter", new LinterStage(LinterStage.Mode.FULL, programContext)))
                    .then(scopedStages
                            .create("Inject ProgramContext", new ProgramContextInjectStage(programContext)))
                    .then(scopedStages
                            .create("Function adding", new FunctionAddingStage()))
                    .thenConditional(GENERATE_MERMAID_TEXT, scopedStages
                            .create("Mermaid", new MermaidStage()))
                    .then(scopedStages
                            .create("Execution", new ExecutionStage()))
                    .perform(stagesData, input);
        } catch (WdlParserException ex) {
            final var error = new ParseErrorsFormatterStage()
                    .perform(stagesData, ex.getParseErrors());
            System.err.println(error);
        } catch (Exception ex) {
            Console.handleException(stagesData, Thread.currentThread(), ex);
        } finally {
            System.out.println();
            System.out.println(stagesData.getOrDefault(OptimizationStage.TAG_OPTIMIZATION_SUMMARY, ""));
        }

        System.out.println("======================");
        System.out.println(measurement.summary(TimeUnit.MILLISECONDS, true));
    }
}
