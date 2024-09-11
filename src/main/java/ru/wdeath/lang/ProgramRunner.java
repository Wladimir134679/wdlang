package ru.wdeath.lang;

import ru.wdeath.lang.ast.Statement;
import ru.wdeath.lang.exception.WdlParserException;
import ru.wdeath.lang.parser.linters.LinterStage;
import ru.wdeath.lang.parser.opttimization.OptimizationStage;
import ru.wdeath.lang.stages.ScopedStageFactory;
import ru.wdeath.lang.stages.StagesDataMap;
import ru.wdeath.lang.stages.impl.*;
import ru.wdeath.lang.utils.Console;
import ru.wdeath.lang.utils.Input.InputSource;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.TimeMeasurement;

import java.util.concurrent.TimeUnit;

public class ProgramRunner {

    private final TimeMeasurement measurement = new TimeMeasurement();
    private final ScopedStageFactory scopedStages = new ScopedStageFactory(measurement::start, measurement::stop);
    private final StagesDataMap stagesData = new StagesDataMap();
    private final ProgramContext programContext;
    private Statement statementProgramInit;
    private InputSource input;
    private final ProgramRunnerConfig programRunnerConfig;

    public ProgramRunner(ProgramRunnerConfig programRunnerConfig, InputSource input) {
        this.input = input;
        this.programRunnerConfig = programRunnerConfig;
        this.programContext = new ProgramContext();
    }

    public InputSource getInput() {
        return input;
    }

    public void setInput(InputSource input) {
        this.input = input;
    }

    public ProgramRunnerConfig getConfig() {
        return programRunnerConfig;
    }

    public void init() {
        try {
            stagesData.put(SourceLoaderStage.TAG_SOURCE_LINES, input);
            statementProgramInit = scopedStages.create("Load source", new SourceLoaderStage())
                    .then(scopedStages
                            .create("Lexer", new LexerStage()))
                    .then(scopedStages
                            .create("Parser", new ParserStage()))
                    .thenConditional(getConfig().optimization, scopedStages
                            .create("Optimization", new OptimizationStage(programRunnerConfig.levelOptimization, programRunnerConfig.showOptimization)))
                    .then(scopedStages
                            .create("Linter", new LinterStage(LinterStage.Mode.FULL, programContext)))
                    .then(scopedStages
                            .create("Inject ProgramContext", new ProgramContextInjectStage(programContext)))
                    .then(scopedStages
                            .create("Function adding", new FunctionAddingStage()))
                    .thenConditional(getConfig().generateMermaidText, scopedStages
                            .create("Mermaid", new MermaidStage()))
                    .perform(stagesData, input);
        } catch (WdlParserException ex) {
            final var error = new ParseErrorsFormatterStage(programContext)
                    .perform(stagesData, ex.getParseErrors());
            System.err.println(error);
        } catch (Exception ex) {
            Console.handleException(programContext, stagesData, Thread.currentThread(), ex);
        } finally {
            if (programRunnerConfig.showOptimization)
                System.out.println(stagesData.getOrDefault(OptimizationStage.TAG_OPTIMIZATION_SUMMARY, ""));
        }
    }

    public void run() {
        try {
            scopedStages.create("Execution", new ExecutionStage())
                    .perform(stagesData, statementProgramInit);
        } catch (WdlParserException ex) {
            final var error = new ParseErrorsFormatterStage(programContext)
                    .perform(stagesData, ex.getParseErrors());
            System.err.println(error);
        } catch (Exception ex) {
            Console.handleException(programContext, stagesData, Thread.currentThread(), ex);
        }
    }

    public ProgramContext resultAndPrint() {
        int lenBar = 30;
        if (getConfig().showMeasurement) {
            System.out.println(printTitle("Time runners", lenBar));
            System.out.println(measurement.summary(TimeUnit.MILLISECONDS, true));
        }
        if (getConfig().showProgramContext) {
            System.out.println(printTitle("Classes", lenBar));
            programContext.getScope().classDeclarations().forEach((s, cl) -> System.out.println(s + " = " + cl));
            System.out.println(printTitle("Functions", lenBar));
            programContext.getScope().functions().forEach((s, function) -> System.out.println(s + " = " + function));
            System.out.println(printTitle("Variables", lenBar));
            programContext.getScope().variables().forEach((s, value) -> System.out.println(s + " = " + value));
        }
        return programContext;
    }

    public static String printTitle(String text, int max) {
        int p2 = max / 2;
        int len = text.length();
        int p2c = p2 - len / 2 - 1;
        return "=".repeat(p2c) + " " + text + " " + "=".repeat(p2c);
    }
}
