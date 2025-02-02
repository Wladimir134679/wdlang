package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ValueUtils {

    private ValueUtils() { }


    public static Number getNumber(Value value) {
        if (value.type() == Types.NUMBER) return ((NumberValue) value).raw();
        return value.asInt();
    }

    public static float getFloatNumber(Value value) {
        if (value.type() == Types.NUMBER) return ((NumberValue) value).raw().floatValue();
        return (float) value.asDouble();
    }

    public static byte[] toByteArray(ArrayValue array) {
        final int size = array.size();
        final byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte) array.get(i).asInt();
        }
        return result;
    }

    public static MapValue consumeMap(Value value, int argumentNumber) {
        final int type = value.type();
        if (type != Types.MAP) {
            throw new TypeException("Map expected at argument " + (argumentNumber + 1)
                    + ", but found " + Types.typeToString(type));
        }
        return (MapValue) value;
    }

    public static Function consumeFunction(Value value, int argumentNumber) {
        return consumeFunction(value, " at argument " + (argumentNumber + 1));
    }

    public static Function consumeFunction(Value value, String errorMessage) {
        final int type = value.type();
        if (type != Types.FUNCTION) {
            throw new TypeException("Function expected" + errorMessage
                    + ", but found " + Types.typeToString(type));
        }
        return ((FunctionValue) value).getFunction();
    }

    public static <T extends Number> MapValue collectNumberConstants(Class<?> clazz, Class<T> type) {
        return collectConstants(clazz, type, NumberValue::of);
    }

    public static <T extends String> MapValue collectStringConstants(Class<?> clazz) {
        return collectConstants(clazz, String.class, StringValue::new);
    }

    @SuppressWarnings("unchecked")
    private static <T, V extends Value> MapValue collectConstants(Class<?> clazz, Class<T> type, FieldConverter<? super T, ? extends V> converter) {
        MapValue result = new MapValue(20);
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!field.getType().equals(type)) continue;
            try {
                result.set(field.getName(), converter.convert((T) field.get(type)));
            } catch (IllegalAccessException ignore) {
            }
        }
        return result;
    }

    private interface FieldConverter<T, V> {
        V convert(T input) throws IllegalAccessException;
    }
}
