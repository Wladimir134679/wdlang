package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayValue implements Value, Iterable<Value>{

    public static ArrayValue add(ArrayValue array, Value value) {
        final int last = array.elements.length;
        final ArrayValue result = new ArrayValue(last + 1);
        System.arraycopy(array.elements, 0, result.elements, 0, last);
        result.elements[last] = value;
        return result;
    }

    public static ArrayValue merge(ArrayValue array1, ArrayValue array2) {
        final int length1 = array1.elements.length;
        final int length2 = array2.elements.length;
        final int length = length1 + length2;
        final ArrayValue result = new ArrayValue(length);
        System.arraycopy(array1.elements, 0, result.elements, 0, length1);
        System.arraycopy(array2.elements, 0, result.elements, length1, length2);
        return result;
    }

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

    public int size() {
        return elements.length;
    }

    @Override
    public Object asJavaObject() {
        return Arrays.stream(elements).map(Value::asJavaObject).toArray(Object[]::new);
    }

    @Override
    public Object raw() {
        return elements;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast array to integer");
    }


    @Override
    public double asDouble() {
        throw new TypeException("Cannot cast array to number");
    }

    @Override
    public String asString() {
        return Arrays.toString(Arrays.stream(elements).map(Value::asString).toArray());
    }

    @Override
    public int type() {
        return Types.ARRAY;
    }

    @Override
    public String toString() {
        return asString();
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

    @Override
    public Iterator<Value> iterator() {
        return Arrays.asList(elements).iterator();
    }

    @Override
    public int compareTo(Value o) {
        return Integer.compare(elements.length, ((Value[])o.raw()).length);
    }
}
