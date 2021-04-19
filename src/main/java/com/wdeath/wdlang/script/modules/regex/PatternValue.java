package com.wdeath.wdlang.script.modules.regex;

import com.wdeath.wdlang.script.exceptions.TypeException;
import com.wdeath.wdlang.script.lib.*;

import java.util.regex.Pattern;

public class PatternValue extends MapValue {

    private final Pattern value;

    public PatternValue(Pattern value) {
        super(8);
        this.value = value;
        init();
    }

    private void init() {
        set("flags", Converters.voidToInt(value::flags));
        set("pattern", Converters.voidToString(value::pattern));
        set("matcher", this::matcher);
        set("matches", this::matches);
        set("split", this::split);
        set("replaceCallback", this::replaceCallback);
    }

    public Pattern getValue() {
        return value;
    }

    private Value split(Value[] args) {
        Arguments.checkOrOr(1, 2, args.length);
        final int limit = (args.length == 2) ? args[1].asInt() : 0;
        return ArrayValue.of(value.split(args[0].asString(), limit));
    }

    private Value matcher(Value[] args) {
        Arguments.check(1, args.length);
        return new MatcherValue(value.matcher(args[0].asString()));
    }

    private Value matches(Value[] args) {
        Arguments.check(1, args.length);
        return NumberValue.fromBoolean(value.matcher(args[0].asString()).matches());
    }

    private Value replaceCallback(Value[] args) {
        Arguments.check(2, args.length);
        if (args[1].type() != Types.FUNCTION) {
            throw new TypeException(args[1].toString() + " is not a function");
        }
        return MatcherValue.replaceCallback(
                new MatcherValue(value.matcher(args[0].asString())),
                ((FunctionValue) args[1]).getValue());
    }
}
