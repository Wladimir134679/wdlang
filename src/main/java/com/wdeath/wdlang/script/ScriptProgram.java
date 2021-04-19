package com.wdeath.wdlang.script;

import com.wdeath.wdlang.script.lib.ClassDeclarations;
import com.wdeath.wdlang.script.lib.Functions;
import com.wdeath.wdlang.script.lib.Variables;
import com.wdeath.wdlang.script.modules.Modules;
import com.wdeath.wdlang.script.parser.Lexer;
import com.wdeath.wdlang.script.parser.Parser;
import com.wdeath.wdlang.script.parser.Token;
import com.wdeath.wdlang.script.parser.ast.Statement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ScriptProgram {

    private String input;
    private Statement rootStatement;

    private ClassDeclarations classes;
    private Variables variables;
    private Functions functions;
    private Modules modules;

    public ScriptProgram(){

    }

    public void init(){
        classes = new ClassDeclarations();
        variables = new Variables();
        functions = new Functions();
        modules = new Modules();
    }

    public void load(String input){
        this.input = input;
        List<Token> tokenize = Lexer.tokenize(input);
        rootStatement = Parser.parse(this, tokenize);
    }

    public void executeRoot(){
        rootStatement.execute();
    }
}
