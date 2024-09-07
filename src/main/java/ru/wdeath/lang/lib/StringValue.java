package ru.wdeath.lang.lib;

import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.exception.UnknownPropertyException;

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
    public Object raw() {
        return value;
    }

    @Override
    public int asInt() {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Value access(Value propertyValue) {
        final String prop = propertyValue.asString();
        return switch (prop) {
            // Properties
            case "length" -> NumberValue.of(length());
            case "lower" -> new StringValue(value.toLowerCase());
            case "upper" -> new StringValue(value.toUpperCase());
            case "chars" -> {
                final Value[] chars = new Value[length()];
                int i = 0;
                for (char ch : value.toCharArray()) {
                    chars[i++] = NumberValue.of(ch);
                }
                yield new ArrayValue(chars);
            }
            // Functions
            case "trim" -> Converters.voidToString(value::trim);
            case "startsWith" -> new FunctionValue(args -> {
                ArgumentsUtil.checkOrOr(1, 2, args.length);
                int offset = (args.length == 2) ? args[1].asInt() : 0;
                return NumberValue.fromBoolean(value.startsWith(args[0].asString(), offset));
            });
            case "endsWith" -> Converters.stringToBoolean(value::endsWith);
            case "matches" -> Converters.stringToBoolean(value::matches);
            case "contains" -> Converters.stringToBoolean(value::contains);
            case "equalsIgnoreCase" -> Converters.stringToBoolean(value::equalsIgnoreCase);
            case "isEmpty" -> Converters.voidToBoolean(value::isEmpty);


            default -> {
                if (ScopeHandler.isFunctionExists(prop)) {
                    final Function f = ScopeHandler.getFunction(prop);
                    yield new FunctionValue(args -> {
                        final Value[] newArgs = new Value[args.length + 1];
                        newArgs[0] = this;
                        System.arraycopy(args, 0, newArgs, 1, args.length);
                        return f.execute(newArgs);
                    });
                }
                throw new UnknownPropertyException(prop);
            }
        };
    }

    public int length() {
        return value.length();
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
        return asString();
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

    @Override
    public int compareTo(Value o) {
        return value.compareTo(o.asString());
    }
}
