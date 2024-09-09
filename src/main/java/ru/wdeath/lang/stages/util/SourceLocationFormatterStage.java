package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.utils.Console;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.Pos;
import ru.wdeath.lang.utils.Range;

public class SourceLocationFormatterStage implements Stage<Range, String> {

    @Override
    public String perform(StagesData stagesData, Range input) {
        final var lines = stagesData.getOrDefault(SourceLoaderStage.TAG_SOURCE_LINES, new String[0]);
        final var sb = new StringBuilder();
        if (input != null && lines.length > 0) {
            printPosition(sb, input.normalize(), lines);
        }
        return sb.toString();
    }

    static void printPosition(StringBuilder sb, Range range, String[] lines) {
        final Pos start = range.start();
        final int linesCount = lines.length;
        ;
        if (range.isOnSameLine()) {
            if (start.row() < linesCount) {
                sb.append(lines[start.row()]);
                sb.append(Console.newline());
                sb.append(" ".repeat(start.col()));
                sb.append("^".repeat(range.end().col() - start.col() + 1));
                sb.append(Console.newline());
            }
        } else {
            if (start.row() < linesCount) {
                String line = lines[start.row()];
                sb.append(line);
                sb.append(Console.newline());
                sb.append(" ".repeat(start.col()));
                sb.append("^".repeat(Math.max(1, line.length() - start.col())));
                sb.append(Console.newline());
            }
            final Pos end = range.end();
            if (end.row() < linesCount) {
                sb.append(lines[end.row()]);
                sb.append(Console.newline());
                sb.append("^".repeat(end.col()));
                sb.append(Console.newline());
            }
        }
    }
}
