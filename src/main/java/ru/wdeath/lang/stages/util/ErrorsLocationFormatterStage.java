package ru.wdeath.lang.stages.util;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;
import ru.wdeath.lang.utils.Console;
import ru.wdeath.lang.utils.Input.SourceLoaderStage;
import ru.wdeath.lang.utils.Pos;
import ru.wdeath.lang.utils.Range;

import java.util.HashSet;

import static ru.wdeath.lang.stages.util.SourceLocationFormatterStage.printPosition;

public class ErrorsLocationFormatterStage implements Stage<Iterable<? extends SourceLocatedError>, String> {

    public static final String TAG_POSITIONS = "formattedPositions";

    private final ProgramContext programContext;

    public ErrorsLocationFormatterStage(ProgramContext programContext) {
        this.programContext = programContext;
    }

    @Override
    public String perform(StagesData stagesData, Iterable<? extends SourceLocatedError> input) {
        final var sb = new StringBuilder();
        final var lines = stagesData.getOrDefault(SourceLoaderStage.TAG_SOURCE_LINES, new String[0]);
        Console console = programContext.getConsole();
        for (SourceLocatedError error : input) {
            sb.append(console.newline());
            sb.append(error);
            final Range range = error.getRange();
            if (range != null) {
                sb.append(' ').append(range.format());
            }
            sb.append(console.newline());
            if (range != null && lines.length > 0) {
                var positions = stagesData.getOrDefault(TAG_POSITIONS, HashSet::new);
                positions.add(range);
                stagesData.put(TAG_POSITIONS, positions);
                printPosition(console, sb, range.normalize(), lines);
            }
        }
        return sb.toString();
    }
}
