package ru.wdeath.lang.lib;

import java.util.Arrays;

public class ArrayValue implements Value{

    private final Value[] elements;

    public ArrayValue(int size) {
        elements = new Value[size];
    }

    public ArrayValue(Value[] elements) {
        this.elements = new Value[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }

    public ArrayValue(ArrayValue other) {
        this(other.elements);
    }

    public Value get(int index) {
        return elements[index];
    }

    public void set(int index, Value value) {
        elements[index] = value;
    }

    @Override
    public double asDouble() {
        throw new RuntimeException("Cannot cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(Arrays.stream(elements).map(Value::asString).toArray());
    }

    @Override
    public String toString() {
        return "ArrayValue{" +
                "e=" + Arrays.toString(elements) +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.deepHashCode(this.elements);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final ArrayValue other = (ArrayValue) obj;
        return Arrays.deepEquals(this.elements, other.elements);
    }
}
