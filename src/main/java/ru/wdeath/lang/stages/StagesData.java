package ru.wdeath.lang.stages;

public interface StagesData {

    <T> T get(String tag);

    void put(String tag, Object value);
}
