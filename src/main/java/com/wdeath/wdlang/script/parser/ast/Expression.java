package com.wdeath.wdlang.script.parser.ast;


import com.wdeath.wdlang.script.lib.Value;

public interface Expression extends Node {
    
    Value eval();
}
