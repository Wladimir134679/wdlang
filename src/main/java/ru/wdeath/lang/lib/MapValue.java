package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MapValue implements Value, Iterable<Map.Entry<Value, Value>> {

    private final Map<Value, Value> map;

    public MapValue(int size) {
        map = new HashMap<>(size);
    }

    public MapValue(Map<Value, Value> map) {
        this.map = map;
    }

    public Value get(Value key) {
        return map.get(key);
    }

    public void set(Value key, Value value) {
        map.put(key, value);
    }

    @Override
    public double asDouble() {
        throw new TypeException("Not implemented");
    }

    @Override
    public int type() {
        return Types.MAP;
    }

    @Override
    public String asString() {
        return map.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapValue mapValue = (MapValue) o;
        return Objects.equals(map, mapValue.map);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.map);
        return hash;
    }

    @Override
    public Iterator<Map.Entry<Value, Value>> iterator() {
        return map.entrySet().iterator();
    }
}
