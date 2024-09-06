package ru.wdeath.lang.stages;

import java.util.HashMap;
import java.util.Map;

public class StagesDataMap implements StagesData{

    private final Map<String, Object> data = new HashMap<>();

    @Override
    public <T> T get(String tag) {
        return (T) data.get(tag);
    }

    @Override
    public void put(String tag, Object value) {
        data.put(tag, value);
    }

    public <T> T getOrDefault(String tag, T defaultValue) {
        return (T) data.getOrDefault(tag, defaultValue);
    }
}
