package com.wdeath.wdlang.script.lib;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.parser.ast.Arguments;
import com.wdeath.wdlang.script.parser.ast.Statement;

public class ClassMethod extends UserDefinedFunction {

    public final ScriptProgram scriptProgram;
    public final ClassInstanceValue classInstance;
    
    public ClassMethod(ScriptProgram scriptProgram, Statement body, Arguments arguments, ClassInstanceValue classInstance) {
        super(scriptProgram, arguments, body);
        this.scriptProgram = scriptProgram;
        this.classInstance = classInstance;
    }
    
    @Override
    public Value execute(Value[] values) {
        scriptProgram.getVariables().push();
        scriptProgram.getVariables().define("this", classInstance.getThisMap());
        
        try {
            return super.execute(values);
        } finally {
            scriptProgram.getVariables().pop();
        }
    }
}
