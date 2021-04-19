package com.wdeath.wdlang.script.modules.json;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.modules.Module;

public final class json implements Module {


    @Override
    public void init(ScriptProgram scriptProgram) {
        scriptProgram.getFunctions().set("jsonencode", new json_encode());
        scriptProgram.getFunctions().set("jsondecode", new json_decode());
    }
}
