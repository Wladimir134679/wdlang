package com.wdeath.wdlang.script;

import com.wdeath.wdlang.script.lib.*;
import com.wdeath.wdlang.script.modules.Modules;
import com.wdeath.wdlang.script.parser.Lexer;
import com.wdeath.wdlang.script.parser.Parser;
import com.wdeath.wdlang.script.parser.Token;
import com.wdeath.wdlang.script.parser.ast.Statement;
import com.wdeath.wdlang.script.parser.visitors.ClassAdder;
import com.wdeath.wdlang.script.parser.visitors.FunctionAdder;
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

    public ScriptProgram init(){
        classes = new ClassDeclarations();
        variables = new Variables();
        functions = new Functions();
        modules = new Modules();
        return this;
    }

    public ScriptProgram load(String input){
        this.input = input;
        List<Token> tokenize = Lexer.tokenize(input);
        rootStatement = Parser.parse(this, tokenize);
        rootStatement.accept(new FunctionAdder());
        rootStatement.accept(new ClassAdder());
        return this;
    }

    public ScriptProgram executeRoot(){
        rootStatement.execute();
        return this;
    }

    public Value execute(String name, Value ... args){
        if(!functions.isExists(name))
            return NumberValue.ZERO;
        return functions.get(name).execute(args);
    }
}
