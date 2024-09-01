package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;

import java.util.Objects;

public class StringValue implements Value {

    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public double asDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public int asInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public int type() {
        return Types.STRING;
    }

    @Override
    public String toString() {
        return "SV{" +
                "v='" + value + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final StringValue other = (StringValue) obj;
        return Objects.equals(this.value, other.value);
    }
}
