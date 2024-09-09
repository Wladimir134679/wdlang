package ru.wdeath.lang;

import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.lib.CallStack;
import ru.wdeath.lang.parser.linters.LinterStage;
import ru.wdeath.lang.parser.opttimization.OptimizationStage;
import ru.wdeath.lang.stages.ScopedStageFactory;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.stages.StagesDataMap;
import ru.wdeath.lang.stages.impl.*;
import ru.wdeath.lang.stages.util.ErrorsLocationFormatterStage;
import ru.wdeath.lang.stages.util.ExceptionConverterStage;
import ru.wdeath.lang.stages.util.ExceptionStackTraceToStringStage;
import ru.wdeath.lang.stages.util.SourceLocationFormatterStage;
import ru.wdeath.lang.utils.Input.InputSourceFile;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.TimeMeasurement;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import static ru.wdeath.lang.stages.util.ErrorsLocationFormatterStage.TAG_POSITIONS;

public class Main {

    public static void main(String[] args) throws IOException {
        final var measurement = new TimeMeasurement();
        final var scopedStages = new ScopedStageFactory(measurement::start, measurement::stop);
        final var input = new InputSourceFile("./examples/classes.wdl");
        final var stagesData = new StagesDataMap();
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
                            .create("Linter", new LinterStage(LinterStage.Mode.FULL)))
                    .then(scopedStages
                            .create("Function adding", new FunctionAddingStage()))
                    .then(scopedStages
                            .create("Execution", new ExecutionStage()))
                    .perform(stagesData, input);
        } catch (WdlParserException ex) {
            final var error = new ParseErrorsFormatterStage()
                    .perform(stagesData, ex.getParseErrors());
            System.err.println(error);
        } catch (Exception ex) {
            handleException(stagesData, Thread.currentThread(), ex);
//            ex.printStackTrace();
        } finally {
            System.out.println();
            System.out.println(stagesData.getOrDefault(OptimizationStage.TAG_OPTIMIZATION_SUMMARY, ""));
        }

        System.out.println("======================");
        System.out.println(measurement.summary(TimeUnit.MILLISECONDS, true));
    }

    public static void handleException(StagesData stagesData, Thread thread, Exception exception) {
        final var joiner = new StringJoiner("\n");
        joiner.add(new ExceptionConverterStage()
                .then((data, error) -> List.of(error))
                .then(new ErrorsLocationFormatterStage())
                .perform(stagesData, exception));
        final var processedPositions = stagesData.getOrDefault(TAG_POSITIONS, HashSet::new);
        if (processedPositions.isEmpty()) {
            // In case no source located errors were printed
            // Find closest SourceLocated call stack frame
            CallStack.getCalls().stream()
                    .limit(4)
                    .map(CallStack.CallInfo::range)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(range -> new SourceLocationFormatterStage()
                            .perform(stagesData, range))
                    .ifPresent(joiner::add);
        }
        joiner.add("Thread: " + thread.getName());
        joiner.add(CallStack.getFormattedCalls());
        joiner.add(new ExceptionStackTraceToStringStage()
                .perform(stagesData, exception));
        System.err.println(joiner.toString());
    }
}
