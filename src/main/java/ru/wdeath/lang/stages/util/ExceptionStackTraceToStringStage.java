package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ExceptionStackTraceToStringStage implements Stage<Exception, String> {

    @Override
    public String perform(StagesData stagesData, Exception ex) {
        final var baos = new ByteArrayOutputStream();
        try (final PrintStream ps = new PrintStream(baos)) {
            for (StackTraceElement traceElement : ex.getStackTrace()) {
                ps.println("\tat " + traceElement);
            }
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}
