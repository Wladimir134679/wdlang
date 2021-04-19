package com.wdeath.wdlang.script.modules.math;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.modules.Module;
import com.wdeath.wdlang.script.lib.*;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

public final class math implements Module {

    private static final DoubleFunction<NumberValue> doubleToNumber = NumberValue::of;

    @Override
    public void init(ScriptProgram scriptProgram) {
        scriptProgram.getVariables().define("PI", NumberValue.of(Math.PI));
        scriptProgram.getVariables().define("E", NumberValue.of(Math.E));

        scriptProgram.getFunctions().set("abs", math::abs);
        scriptProgram.getFunctions().set("acos", functionConvert(Math::acos));
        scriptProgram.getFunctions().set("asin", functionConvert(Math::asin));
        scriptProgram.getFunctions().set("atan", functionConvert(Math::atan));
        scriptProgram.getFunctions().set("atan2", biFunctionConvert(Math::atan2));
        scriptProgram.getFunctions().set("cbrt", functionConvert(Math::cbrt));
        scriptProgram.getFunctions().set("ceil", functionConvert(Math::ceil));
        scriptProgram.getFunctions().set("copySign", math::copySign);
        scriptProgram.getFunctions().set("cos", functionConvert(Math::cos));
        scriptProgram.getFunctions().set("cosh", functionConvert(Math::cosh));
        scriptProgram.getFunctions().set("exp", functionConvert(Math::exp));
        scriptProgram.getFunctions().set("expm1", functionConvert(Math::expm1));
        scriptProgram.getFunctions().set("floor", functionConvert(Math::floor));
        scriptProgram.getFunctions().set("getExponent", math::getExponent);
        scriptProgram.getFunctions().set("hypot", biFunctionConvert(Math::hypot));
        scriptProgram.getFunctions().set("IEEEremainder", biFunctionConvert(Math::IEEEremainder));
        scriptProgram.getFunctions().set("log", functionConvert(Math::log));
        scriptProgram.getFunctions().set("log1p", functionConvert(Math::log1p));
        scriptProgram.getFunctions().set("log10", functionConvert(Math::log10));
        scriptProgram.getFunctions().set("max", math::max);
        scriptProgram.getFunctions().set("min", math::min);
        scriptProgram.getFunctions().set("nextAfter", math::nextAfter);
        scriptProgram.getFunctions().set("nextUp", functionConvert(Math::nextUp, Math::nextUp));
        scriptProgram.getFunctions().set("nextDown", functionConvert(Math::nextDown, Math::nextDown));
        scriptProgram.getFunctions().set("pow", biFunctionConvert(Math::pow));
        scriptProgram.getFunctions().set("rint", functionConvert(Math::rint));
        scriptProgram.getFunctions().set("round", math::round);
        scriptProgram.getFunctions().set("signum", functionConvert(Math::signum, Math::signum));
        scriptProgram.getFunctions().set("sin", functionConvert(Math::sin));
        scriptProgram.getFunctions().set("sinh", functionConvert(Math::sinh));
        scriptProgram.getFunctions().set("sqrt", functionConvert(Math::sqrt));
        scriptProgram.getFunctions().set("tan", functionConvert(Math::tan));
        scriptProgram.getFunctions().set("tanh", functionConvert(Math::tanh));
        scriptProgram.getFunctions().set("toDegrees", functionConvert(Math::toDegrees));
        scriptProgram.getFunctions().set("toRadians", functionConvert(Math::toRadians));
        scriptProgram.getFunctions().set("ulp", functionConvert(Math::ulp, Math::ulp));
    }

    private static Value abs(Value... args) {
        Arguments.check(1, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Double) {
            return NumberValue.of(Math.abs((double) raw));
        }
        if (raw instanceof Float) {
            return NumberValue.of(Math.abs((float) raw));
        }
        if (raw instanceof Long) {
            return NumberValue.of(Math.abs((long) raw));
        }
        return NumberValue.of(Math.abs(args[0].asInt()));
    }

    private static Value copySign(Value... args) {
        Arguments.check(2, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Float) {
            return NumberValue.of(Math.copySign((float) raw, ((NumberValue) args[1]).asFloat()));
        }
        return NumberValue.of(Math.copySign(args[0].asNumber(), args[1].asNumber()));
    }

    private static Value getExponent(Value... args) {
        Arguments.check(1, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Float) {
            return NumberValue.of(Math.getExponent((float) raw));
        }
        return NumberValue.of(Math.getExponent(args[0].asNumber()));
    }

    private static Value max(Value... args) {
        Arguments.check(2, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Double) {
            return NumberValue.of(Math.max((double) raw, args[1].asNumber()));
        }
        if (raw instanceof Float) {
            return NumberValue.of(Math.max((float) raw, ((NumberValue) args[1]).asFloat()));
        }
        if (raw instanceof Long) {
            return NumberValue.of(Math.max((long) raw, ((NumberValue) args[1]).asLong()));
        }
        return NumberValue.of(Math.max(args[0].asInt(), args[1].asInt()));
    }

    private static Value min(Value... args) {
        Arguments.check(2, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Double) {
            return NumberValue.of(Math.min((double) raw, args[1].asNumber()));
        }
        if (raw instanceof Float) {
            return NumberValue.of(Math.min((float) raw, ((NumberValue) args[1]).asFloat()));
        }
        if (raw instanceof Long) {
            return NumberValue.of(Math.min((long) raw, ((NumberValue) args[1]).asLong()));
        }
        return NumberValue.of(Math.min(args[0].asInt(), args[1].asInt()));
    }

    private static Value nextAfter(Value... args) {
        Arguments.check(2, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Float) {
            return NumberValue.of(Math.nextAfter((float) raw, args[1].asNumber()));
        }
        return NumberValue.of(Math.nextAfter(args[0].asNumber(), args[1].asNumber()));
    }

    private static Value round(Value... args) {
        Arguments.check(1, args.length);
        final Object raw = args[0].raw();
        if (raw instanceof Float) {
            return NumberValue.of(Math.round((float) raw));
        }
        return NumberValue.of(Math.round(args[0].asNumber()));
    }


    private static Function functionConvert(DoubleUnaryOperator op) {
        return args -> {
            Arguments.check(1, args.length);
            return doubleToNumber.apply(op.applyAsDouble(args[0].asNumber()));
        };
    }

    private static Function functionConvert(DoubleUnaryOperator opDouble, UnaryOperator<Float> opFloat) {
        return args -> {
            Arguments.check(1, args.length);
            final Object raw = args[0].raw();
            if (raw instanceof Float) {
                return NumberValue.of(opFloat.apply((float) raw));
            }
            return NumberValue.of(opDouble.applyAsDouble(args[0].asNumber()));
        };
    }

    private static Function biFunctionConvert(DoubleBinaryOperator op) {
        return args -> {
            Arguments.check(2, args.length);
            return doubleToNumber.apply(op.applyAsDouble(args[0].asNumber(), args[1].asNumber()));
        };
    }
}
