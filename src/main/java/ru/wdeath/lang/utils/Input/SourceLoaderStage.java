package ru.wdeath.lang.utils.Input;

import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.stages.Stage;
import ru.wdeath.lang.stages.StagesData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SourceLoaderStage implements Stage<InputSource, String> {

    public static final String TAG_SOURCE_LINES = "sourceLines";

    @Override
    public String perform(StagesData stagesData, InputSource inputSource) {
        try {
            String result = inputSource.load();
            final var lines = (result == null || result.isEmpty())
                    ? new String[0]
                    : result.split("\r?\n");
            stagesData.put(TAG_SOURCE_LINES, lines);
            return result;
        } catch (IOException e) {
            throw new WdlRuntimeException("Unable to read input " + inputSource, e);
        }
    }

    public static String readStream(InputStream is) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final int bufferSize = 1024;
        final byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = is.read(buffer)) != -1) {
            result.write(buffer, 0, read);
        }
        return result.toString(StandardCharsets.UTF_8);
    }
}
