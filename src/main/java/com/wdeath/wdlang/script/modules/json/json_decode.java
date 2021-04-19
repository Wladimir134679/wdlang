package com.wdeath.wdlang.script.modules.json;

import com.wdeath.wdlang.script.lib.Arguments;
import com.wdeath.wdlang.script.lib.Function;
import com.wdeath.wdlang.script.lib.Value;
import com.wdeath.wdlang.script.lib.ValueUtils;
import org.json.JSONException;
import org.json.JSONTokener;

public final class json_decode implements Function {

    @Override
    public Value execute(Value... args) {
        Arguments.check(1, args.length);
        try {
            final String jsonRaw = args[0].asString();
            final Object root = new JSONTokener(jsonRaw).nextValue();
            return ValueUtils.toValue(root);
        } catch (JSONException ex) {
            throw new RuntimeException("Error while parsing json", ex);
        }
    }
}
