package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class MapValue implements Value, Iterable<Map.Entry<Value, Value>> {

    private final Map<Value, Value> map;

    public static MapValue merge(MapValue map1, MapValue map2) {
        final MapValue result = new MapValue(map1.size() + map2.size());
        result.map.putAll(map1.map);
        result.map.putAll(map2.map);
        return result;
    }

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

    public int size() {
        return map.size();
    }

    public boolean ifPresent(String key, Consumer<Value> consumer) {
        return ifPresent(new StringValue(key), consumer);
    }

    public boolean ifPresent(Value key, Consumer<Value> consumer) {
        if (map.containsKey(key)) {
            consumer.accept(map.get(key));
            return true;
        }
        return false;
    }


    @Override
    public Object raw() {
        return map;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast array to integer");
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

    @Override
    public int compareTo(Value o) {
        return Integer.compare(map.size(), ((Map<?, ?>) o.raw()).size());
    }

    @Override
    public String toString() {
        return "MapValue{" +
                "map=" + map +
                '}';
    }
}
