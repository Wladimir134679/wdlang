package ru.wdeath.lang.stages.impl;

import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceLoaderStage implements Stage<String, String> {

    public static final String TAG_SOURCE = "source";

    @Override
    public String perform(StagesData stagesData, String input) {
        try {
            String result = Files.readString(Path.of(input));
            stagesData.put(TAG_SOURCE, result);
            return result;
        } catch (IOException e) {
            throw new WdlRuntimeException("Unable to read input " + input, e);
        }
    }
}
