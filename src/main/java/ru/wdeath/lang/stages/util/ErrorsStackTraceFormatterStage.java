package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.utils.Console;


public class ErrorsStackTraceFormatterStage implements Stage<Iterable<? extends SourceLocatedError>, String> {

    private final ProgramContext programContext;

    public ErrorsStackTraceFormatterStage(ProgramContext programContext) {
        this.programContext = programContext;
    }

    @Override
    public String perform(StagesData stagesData, Iterable<? extends SourceLocatedError> input) {
        final var sb = new StringBuilder();
        for (SourceLocatedError error : input) {
            if (!error.hasStackTrace()) continue;
            for (StackTraceElement el : error.getStackTrace()) {
                sb.append("\t").append(el).append(programContext.getConsole().newline());
            }
        }
        return sb.toString();
    }
}
