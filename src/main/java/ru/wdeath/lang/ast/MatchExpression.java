package ru.wdeath.lang.ast;

import ru.wdeath.lang.exception.PatternMatchingException;
import ru.wdeath.lang.lib.NumberValue;
import ru.wdeath.lang.lib.ScopeHandler;
import ru.wdeath.lang.lib.Value;

import java.util.List;

public class MatchExpression implements Expression, Statement{


    public final Expression expression;
    public final List<Pattern> patterns;

    public MatchExpression(Expression expression, List<Pattern> patterns) {
        this.expression = expression;
        this.patterns = patterns;
    }

    @Override
    public void execute() {
        eval();
    }

    @Override
    public Value eval() {
        final Value value = expression.eval();
        for (Pattern p : patterns) {
            if (p instanceof ConstantPattern pattern) {
                if (match(value, pattern.constant) && optMatches(p)) {
                    return evalResult(p.result);
                }
            }
            if (p instanceof VariablePattern pattern) {
                if (pattern.variable.equals("_")) return evalResult(p.result);

                if (ScopeHandler.isVariableOrConstantExists(pattern.variable)) {
                    if (match(value, ScopeHandler.getVariableOrConstant(pattern.variable)) && optMatches(p)) {
                        return evalResult(p.result);
                    }
                } else {
                    ScopeHandler.defineVariableInCurrentScope(pattern.variable, value);
                    if (optMatches(p)) {
                        final Value result = evalResult(p.result);;
                        ScopeHandler.removeVariable(pattern.variable);
                        return result;
                    }
                    ScopeHandler.removeVariable(pattern.variable);
                }
            }
        }
        throw new PatternMatchingException("No pattern were matched");
    }

    private boolean match(Value value, Value constant) {
        if (value.type() != constant.type()) return false;
        return value.equals(constant);
    }

    private boolean optMatches(Pattern pattern) {
        if (pattern.optCondition == null) return true;
        return pattern.optCondition.eval() != NumberValue.ZERO;
    }

    private Value evalResult(Statement s) {
        try {
            s.execute();
        } catch (ReturnStatement ret) {
            return ret.getResult();
        }
        return NumberValue.ZERO;
    }

    @Override
    public <R, T> R accept(ResultVisitor<R, T> visitor, T input) {
        return visitor.visit(this, input);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("match ").append(expression).append(" {");
        for (Pattern p : patterns) {
            sb.append("\n  case ").append(p);
        }
        sb.append("\n}");
        return sb.toString();
    }

    public static abstract class Pattern {
        public Statement result;
        public Expression optCondition;
    }

    public static class ConstantPattern extends Pattern {
        public Value constant;

        public ConstantPattern(Value pattern) {
            this.constant = pattern;
        }

        @Override
        public String toString() {
            return constant + ": " + result;
        }
    }

    public static class VariablePattern extends Pattern {
        public String variable;

        public VariablePattern(String pattern) {
            this.variable = pattern;
        }

        @Override
        public String toString() {
            return variable + ": " + result;
        }
    }
}
