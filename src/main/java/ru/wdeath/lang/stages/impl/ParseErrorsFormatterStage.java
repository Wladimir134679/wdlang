package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.stages.util.ErrorsLocationFormatterStage;
import ru.wdeath.lang.stages.util.ErrorsStackTraceFormatterStage;
import ru.wdeath.lang.stages.util.SourceLocatedError;

import java.util.Collection;

public class ParseErrorsFormatterStage implements Stage<Collection<? extends SourceLocatedError>, String> {

    private final ProgramContext programContext;

    public ParseErrorsFormatterStage(ProgramContext programContext) {
        this.programContext = programContext;
    }

    public String perform(StagesData stagesData, Collection<? extends SourceLocatedError> input) {
        String error = new ErrorsLocationFormatterStage(programContext)
                .perform(stagesData, input);
        String stackTrace = new ErrorsStackTraceFormatterStage(programContext)
                .perform(stagesData, input);
        return error + "\n" + stackTrace;
    }

//    private static void printPosition(StringBuilder sb, Range range, String[] lines) {
//        final Pos start = range.start();
//        final int linesCount = lines.length;;
//        if (range.isOnSameLine()) {
//            if (start.row() < linesCount) {
//                sb.append(lines[start.row()]);
//                sb.append(newline());
//                sb.append(" ".repeat(start.col()));
//                sb.append("^".repeat(range.end().col() - start.col() + 1));
//                sb.append(newline());
//            }
//        } else {
//            if (start.row() < linesCount) {
//                String line = lines[start.row()];
//                sb.append(line);
//                sb.append(newline());
//                sb.append(" ".repeat(start.col()));
//                sb.append("^".repeat(Math.max(1, line.length() - start.col())));
//                sb.append(newline());
//            }
//            final Pos end = range.end();
//            if (end.row() < linesCount) {
//                sb.append(lines[end.row()]);
//                sb.append(newline());
//                sb.append("^".repeat(end.col()));
//                sb.append(newline());
//            }
//        }
//    }
//
//    private static String newline(){
//        return "\n";
//    }
}
