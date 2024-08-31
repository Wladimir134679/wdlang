package ru.wdeath.lang.lib;

public class NumberValue implements Value {

    public static final NumberValue ZERO = new NumberValue(0);
    public static final NumberValue ONE = new NumberValue(1);

    public static NumberValue fromBoolean(boolean b) {
        return b ? ONE : ZERO;
    }

    private final double value;

    public NumberValue(boolean value) {
        this.value = value ? 1 : 0;
    }

    public NumberValue(double value) {
        this.value = value;
    }

    @Override
    public double asDouble() {
        return value;
    }

    @Override
    public String asString() {
        return Double.toString(value);
    }

    @Override
    public String toString() {
        return "NV{" +
                "v=" + value +
                '}';
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Long.hashCode(Double.doubleToLongBits(this.value));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final NumberValue other = (NumberValue) obj;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }
}
