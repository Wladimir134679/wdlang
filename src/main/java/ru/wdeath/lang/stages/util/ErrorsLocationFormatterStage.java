package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.Pos;
import ru.wdeath.lang.utils.Range;

import java.util.HashSet;

public class ErrorsLocationFormatterStage implements Stage<Iterable<? extends SourceLocatedError>, String> {

    public static final String TAG_POSITIONS = "formattedPositions";

    @Override
    public String perform(StagesData stagesData, Iterable<? extends SourceLocatedError> input) {
        final var sb = new StringBuilder();
        final var lines = stagesData.getOrDefault(SourceLoaderStage.TAG_SOURCE_LINES, new String[0]);
        for (SourceLocatedError error : input) {
            sb.append(newline());
            sb.append(error);
            sb.append(newline());
            final Range range = error.getRange();
            if (range != null) {
                var positions = stagesData.getOrDefault(TAG_POSITIONS, HashSet::new);
                positions.add(range);
                stagesData.put(TAG_POSITIONS, positions);
                printPosition(sb, range.normalize(), lines);
            }
        }
        return sb.toString();
    }

    private static void printPosition(StringBuilder sb, Range range, String[] lines) {
        final Pos start = range.start();
        final int linesCount = lines.length;
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
    
    public static String newline(){
        return "\n";
    }
}
