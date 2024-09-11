package ru.wdeath.lang.utils;


import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.lib.CallStack;
import ru.wdeath.lang.outputsettings.ConsoleOutputSettings;
import ru.wdeath.lang.outputsettings.OutputSettings;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.stages.util.ErrorsLocationFormatterStage;
import ru.wdeath.lang.stages.util.ExceptionConverterStage;
import ru.wdeath.lang.stages.util.ExceptionStackTraceToStringStage;
import ru.wdeath.lang.stages.util.SourceLocationFormatterStage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static ru.wdeath.lang.stages.util.ErrorsLocationFormatterStage.TAG_POSITIONS;


public class Console {

    private OutputSettings outputSettings;

    public Console() {
        this(new ConsoleOutputSettings());
    }

    public Console(OutputSettings outputSettings) {
        this.outputSettings = outputSettings;
    }

    public void useSettings(OutputSettings outputSettings) {
        this.outputSettings = outputSettings;
    }

    public OutputSettings getSettings() {
        return outputSettings;
    }

    public String newline() {
        return outputSettings.newline();
    }

    public void print(String value) {
        outputSettings.print(value);
    }

    public void print(Object value) {
        outputSettings.print(value);
    }

    public void println() {
        outputSettings.println();
    }

    public void println(String value) {
        outputSettings.println(value);
    }

    public void println(Object value) {
        outputSettings.println(value);
    }

    public String text() {
        return outputSettings.getText();
    }

    public void error(Throwable throwable) {
        outputSettings.error(throwable);
    }

    public void error(CharSequence value) {
        outputSettings.error(value);
    }

    public File fileInstance(String path) {
        return outputSettings.fileInstance(path);
    }

    public static void handleException(ProgramContext programContext, StagesData stagesData, Thread thread, Exception exception) {
        final var joiner = new StringJoiner("\n");
        joiner.add(new ExceptionConverterStage()
                .then((data, error) -> List.of(error))
                .then(new ErrorsLocationFormatterStage(programContext))
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
                    .map(range -> new SourceLocationFormatterStage(programContext)
                            .perform(stagesData, range))
                    .ifPresent(joiner::add);
        }
        joiner.add("Thread: " + thread.getName());
        joiner.add(CallStack.getFormattedCalls());
        joiner.add(new ExceptionStackTraceToStringStage()
                .perform(stagesData, exception));
        programContext.getConsole().error(joiner.toString());
    }

    public static void handleException(ProgramContext programContext, Thread thread, Throwable throwable) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (final PrintStream ps = new PrintStream(baos)) {
            ps.printf("%s in %s%n", throwable.getMessage(), thread.getName());
            for (CallStack.CallInfo call : CallStack.getCalls()) {
                ps.printf("\tat %s%n", call);
            }
            ps.println();
            throwable.printStackTrace(ps);
            ps.flush();
        }
        programContext.getConsole().error(baos.toString(StandardCharsets.UTF_8));
    }
}
