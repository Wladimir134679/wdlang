package com.wdeath.wdlang.script.parser.optimization;

import com.wdeath.wdlang.script.ScriptProgram;
import com.wdeath.wdlang.script.lib.Value;
import com.wdeath.wdlang.script.parser.ast.*;

import java.util.HashMap;
import java.util.Map;

import static com.wdeath.wdlang.script.parser.visitors.VisitorUtils.isValue;
import static com.wdeath.wdlang.script.parser.visitors.VisitorUtils.isVariable;

public class VariablesGrabber extends OptimizationVisitor<Map<String, VariableInfo>> {

    public static Map<String, VariableInfo> getInfo(Node node, ScriptProgram scriptProgram) {
        return getInfo(node, false, scriptProgram);
    }

    public static Map<String, VariableInfo> getInfo(Node node, boolean grabModuleConstants, ScriptProgram scriptProgram) {
        Map<String, VariableInfo> variableInfos = new HashMap<>();
        node.accept(new VariablesGrabber(grabModuleConstants, scriptProgram), variableInfos);
        return variableInfos;
    }

    private final boolean grabModuleConstants;
    private final ScriptProgram scriptProgram;

    public VariablesGrabber(ScriptProgram scriptProgram) {
        this(false, scriptProgram);
    }

    public VariablesGrabber(boolean grabModuleConstants, ScriptProgram scriptProgram) {
        super(scriptProgram);
        this.grabModuleConstants = grabModuleConstants;
        this.scriptProgram = scriptProgram;
    }

    @Override
    public Node visit(AssignmentExpression s, Map<String, VariableInfo> t) {
        if (!isVariable((Node)s.target)) {
            return super.visit(s, t);
        }

        final String variableName = ((VariableExpression) s.target).name;
        final VariableInfo var = variableInfo(t, variableName);

        if (s.operation == null && isValue(s.expression)) {
            var.value = ((ValueExpression) s.expression).value;
        }
        t.put(variableName, var);
        return super.visit(s, t);
    }

    @Override
    public Node visit(DestructuringAssignmentStatement s, Map<String, VariableInfo> t) {
        for (String variableName : s.variables) {
            if (variableName == null) continue;
            t.put(variableName, variableInfo(t, variableName));
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(ForeachArrayStatement s, Map<String, VariableInfo> t) {
        t.put(s.variable, variableInfo(t, s.variable));
        return super.visit(s, t);
    }

    @Override
    public Node visit(ForeachMapStatement s, Map<String, VariableInfo> t) {
        t.put(s.key, variableInfo(t, s.key));
        t.put(s.value, variableInfo(t, s.value));
        return super.visit(s, t);
    }

    @Override
    public Node visit(MatchExpression s, Map<String, VariableInfo> t) {
        for (MatchExpression.Pattern pattern : s.patterns) {
            if (pattern instanceof MatchExpression.VariablePattern) {
                final String variableName = ((MatchExpression.VariablePattern) pattern).variable;
                t.put(variableName, variableInfo(t, variableName));
            }
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(UnaryExpression s, Map<String, VariableInfo> t) {
        if (s.expr1 instanceof Accessible) {
            if (s.expr1 instanceof VariableExpression) {
                final String variableName = ((VariableExpression) s.expr1).name;
                t.put(variableName, variableInfo(t, variableName));
            }
            if (s.expr1 instanceof ContainerAccessExpression) {
                ContainerAccessExpression conExpr = (ContainerAccessExpression) s.expr1;
                if (conExpr.rootIsVariable()) {
                    final String variableName = ((VariableExpression) conExpr.root).name;
                    t.put(variableName, variableInfo(t, variableName));
                }
            }
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(UseStatement s, Map<String, VariableInfo> t) {
        if (grabModuleConstants) {
            // To get module variables we need  to store current variables, clear all, then load module.
            final Map<String, Value> currentVariables = new HashMap<>(scriptProgram.getVariables().variables());
            scriptProgram.getVariables().variables().clear();
            if (canLoadConstants(s.expression)) {
                s.loadConstants();
            }
            // Grab module variables
            for (Map.Entry<String, Value> entry : scriptProgram.getVariables().variables().entrySet()) {
                final VariableInfo var = variableInfo(t, entry.getKey());
                var.value = entry.getValue();
                t.put(entry.getKey(), var);
            }
            // Restore previous variables
            scriptProgram.getVariables().variables().putAll(currentVariables);
        }
        return super.visit(s, t);
    }

    private boolean canLoadConstants(Expression expression) {
        if (expression instanceof ArrayExpression) {
            ArrayExpression ae = (ArrayExpression) expression;
            for (Expression expr : ae.elements) {
                if (!isValue(expr)) {
                    return false;
                }
            }
            return true;
        }
        return isValue(expression);
    }

    @Override
    protected boolean visit(Arguments in, Arguments out, Map<String, VariableInfo> t) {
        for (Argument argument : in) {
            final String variableName = argument.getName();
            final VariableInfo var = variableInfo(t, variableName);
            /* No need to add value - it is optional arguments
            final Expression expr = argument.getValueExpr();
            if (expr != null && isValue(expr)) {
                var.value = ((ValueExpression) expr).value;
            }*/
            t.put(variableName, var);
        }
        return super.visit(in, out, t);
    }



    private VariableInfo variableInfo(Map<String, VariableInfo> t, final String variableName) {
        final VariableInfo var;
        if (t.containsKey(variableName)) {
            var = t.get(variableName);
            var.modifications++;
        } else {
            var = new VariableInfo();
            var.modifications = 1;
        }
        return var;
    }
}
