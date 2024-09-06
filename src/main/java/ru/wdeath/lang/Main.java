package ru.wdeath.lang;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.parser.opttimization.OptimizationStage;
import ru.wdeath.lang.stages.ScopedStageFactory;
import ru.wdeath.lang.stages.StagesDataMap;
import ru.wdeath.lang.stages.impl.*;
import ru.wdeath.lang.utils.TimeMeasurement;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        final var measurement = new TimeMeasurement();
        final var scopedStages = new ScopedStageFactory(measurement::start, measurement::stop);
        final var input = "./examples/program1.wdl";
        final var stagesData = new StagesDataMap();
        try {
            stagesData.put(OptimizationStage.TAG_OPTIMIZATION_SUMMARY, true);
            stagesData.put(SourceLoaderStage.TAG_SOURCE, input);

            scopedStages.create("Load source", new SourceLoaderStage())
                    .then(scopedStages
                            .create("Lexer", new LexerStage()))
                    .then(scopedStages
                            .create("Parser", new ParserStage()))
                    .thenConditional(true, scopedStages
                            .create("Optimization", new OptimizationStage(10, true)))
                    .then(scopedStages
                            .create("Linter", new LinterStage()))
                    .then(scopedStages
                            .create("Function adding", new FunctionAddingStage()))
                    .then(scopedStages
                            .create("Execution", new ExecutionStage()))
                    .perform(stagesData, input);
        } catch (WdlParserException ex) {
            final var error = new ParseErrorsFormatterStage()
                    .perform(stagesData, ex.getParseErrors());
            System.err.println(error);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            System.out.println();
            System.out.println(stagesData.getOrDefault(OptimizationStage.TAG_OPTIMIZATION_SUMMARY, ""));
        }

        System.out.println("======================");
        System.out.println(measurement.summary(TimeUnit.MILLISECONDS, true));
    }

}
