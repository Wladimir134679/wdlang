package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.parser.Pos;
import ru.wdeath.lang.parser.Range;
import ru.wdeath.lang.parser.error.ParseError;
import ru.wdeath.lang.parser.error.ParseErrors;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

public class ParseErrorsFormatterStage implements Stage<ParseErrors, String> {


    public String perform(StagesData stagesData, ParseErrors input) {
        final var sb = new StringBuilder();
        final String source = stagesData.get(SourceLoaderStage.TAG_SOURCE);
        final var lines = source.split("\r?\n");
        for (ParseError parseError : input) {
            sb.append(newline());
            sb.append(parseError);
            sb.append(newline());
            final Range range = parseError.range().normalize();
            printPosition(sb, range, lines);
            if (parseError.hasStackTrace()) {
                sb.append("Stack trace:");
                sb.append(newline());
                for (StackTraceElement el : parseError.stackTraceElements()) {
                    sb.append("    ").append(el).append(newline());
                }
            }
        }
        return sb.toString();
    }

    private static void printPosition(StringBuilder sb, Range range, String[] lines) {
        final Pos start = range.start();
        final int linesCount = lines.length;;
        if (range.isOnSameLine()) {
            if (start.row() < linesCount) {
                sb.append(lines[start.row()]);
                sb.append(newline());
                sb.append(" ".repeat(start.col()));
                sb.append("^".repeat(range.end().col() - start.col() + 1));
                sb.append(newline());
            }
        } else {
            if (start.row() < linesCount) {
                String line = lines[start.row()];
                sb.append(line);
                sb.append(newline());
                sb.append(" ".repeat(start.col()));
                sb.append("^".repeat(Math.max(1, line.length() - start.col())));
                sb.append(newline());
            }
            final Pos end = range.end();
            if (end.row() < linesCount) {
                sb.append(lines[end.row()]);
                sb.append(newline());
                sb.append("^".repeat(end.col()));
                sb.append(newline());
            }
        }
    }
    
    private static String newline(){
        return "\n";
    }
}
