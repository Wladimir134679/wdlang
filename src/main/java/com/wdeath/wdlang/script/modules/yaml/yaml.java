package com.wdeath.wdlang.script.modules.yaml;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.modules.Module;

public final class yaml implements Module {

    @Override
    public void init(ScriptProgram scriptProgram) {
        scriptProgram.getFunctions().set("yamlencode", new yaml_encode());
        scriptProgram.getFunctions().set("yamldecode", new yaml_decode());
    }
}
