package com.wdeath.wdlang.script.parser.ast;


import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.exceptions.ParseException;
import com.wdeath.wdlang.script.parser.Lexer;
import com.wdeath.wdlang.script.parser.Parser;
import com.wdeath.wdlang.script.parser.SourceLoader;
import com.wdeath.wdlang.script.parser.Token;
import com.wdeath.wdlang.script.parser.visitors.FunctionAdder;

import java.io.IOException;
import java.util.List;

public final class IncludeStatement extends InterruptableNode implements Statement {

    private final ScriptProgram scriptProgram;
    public final Expression expression;
    
    public IncludeStatement(ScriptProgram scriptProgram, Expression expression) {
        this.scriptProgram = scriptProgram;
        this.expression = expression;
    }

    @Override
    public void execute() {
        super.interruptionCheck();
        try {
            final Statement program = loadProgram(expression.eval().asString());
            if (program != null) {
                program.accept(new FunctionAdder());
                program.execute();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Statement loadProgram(String path) throws IOException {
        final String input = SourceLoader.readSource(path);
        final List<Token> tokens = Lexer.tokenize(input);
        final Parser parser = new Parser(scriptProgram, tokens);
        final Statement program = parser.parse();
        if (parser.getParseErrors().hasErrors()) {
            throw new ParseException(parser.getParseErrors().toString());
        }
        return program;
    }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T t) {
        return visitor.visit(this, t);
    }

    @Override
    public String toString() {
        return "include " + expression;
    }
}
