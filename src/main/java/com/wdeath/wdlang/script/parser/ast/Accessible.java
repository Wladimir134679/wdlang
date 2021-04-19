package com.wdeath.wdlang.script.parser.ast;


import com.wdeath.wdlang.script.lib.Value;

public interface Accessible extends Node {

    Value get();
    
    Value set(Value value);
}
