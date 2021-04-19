package com.wdeath.wdlang.script.modules.types;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.modules.Module;
import com.wdeath.wdlang.script.lib.*;

public final class types implements Module {


    @Override
    public void init(ScriptProgram scriptProgram) {
        scriptProgram.getVariables().define("OBJECT", NumberValue.of(Types.OBJECT));
        scriptProgram.getVariables().define("NUMBER", NumberValue.of(Types.NUMBER));
        scriptProgram.getVariables().define("STRING", NumberValue.of(Types.STRING));
        scriptProgram.getVariables().define("ARRAY", NumberValue.of(Types.ARRAY));
        scriptProgram.getVariables().define("MAP", NumberValue.of(Types.MAP));
        scriptProgram.getVariables().define("FUNCTION", NumberValue.of(Types.FUNCTION));
        
        scriptProgram.getFunctions().set("typeof", args -> NumberValue.of(args[0].type()));
        scriptProgram.getFunctions().set("string", args -> new StringValue(args[0].asString()));
        scriptProgram.getFunctions().set("number", args -> NumberValue.of(args[0].asNumber()));
        
        scriptProgram.getFunctions().set("byte", args -> NumberValue.of((byte)args[0].asInt()));
        scriptProgram.getFunctions().set("short", args -> NumberValue.of((short)args[0].asInt()));
        scriptProgram.getFunctions().set("int", args -> NumberValue.of(args[0].asInt()));
        scriptProgram.getFunctions().set("long", args -> NumberValue.of((long)args[0].asNumber()));
        scriptProgram.getFunctions().set("float", args -> NumberValue.of((float)args[0].asNumber()));
        scriptProgram.getFunctions().set("double", args -> NumberValue.of(args[0].asNumber()));
    }
}
