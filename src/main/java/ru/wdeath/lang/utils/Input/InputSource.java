package ru.wdeath.lang.utils.Input;

import java.io.IOException;

public interface InputSource {

    String getPath();

    String load() throws IOException;

    default String getBasePath() {
        int i = getPath().lastIndexOf("/");
        if (i == -1) return "";
        return getPath().substring(0, i + 1);
    }
}
