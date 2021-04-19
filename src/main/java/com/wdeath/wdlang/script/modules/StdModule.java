package com.wdeath.wdlang.script.modules;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.lib.Function;
import com.wdeath.wdlang.script.lib.NumberValue;
import com.wdeath.wdlang.script.lib.Value;

public class StdModule implements Module {

    private Value tmp;

    @Override
    public void init(ScriptProgram scriptProgram) {
        scriptProgram.getFunctions().set("setTmp", this::setTmp);
        scriptProgram.getFunctions().set("getTmp", this::getTmp);
    }

    private Value setTmp(Value ... args){
        tmp = args[0];
        return NumberValue.ZERO;
    }

    private Value getTmp(Value ... args){
        return tmp;
    }
}
