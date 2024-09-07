package ru.wdeath.lang.visitors.optimization;

import ru.wdeath.lang.ast.*;

import java.util.HashMap;
import java.util.Map;

import static ru.wdeath.lang.visitors.VisitorUtils.isValue;
import static ru.wdeath.lang.visitors.VisitorUtils.isVariable;

public class VariablesGrabber extends OptimizationVisitor<Map<String, VariableInfo>> {

    public static Map<String, VariableInfo> getInfo(Node node) {
        return getInfo(node, false);
    }

    public static Map<String, VariableInfo> getInfo(Node node, boolean grabModuleConstants) {
        Map<String, VariableInfo> variableInfos = new HashMap<>();
        node.accept(new VariablesGrabber(grabModuleConstants), variableInfos);
        return variableInfos;
    }

    private final boolean grabModuleConstants;

    public VariablesGrabber() {
        this(false);
    }

    public VariablesGrabber(boolean grabModuleConstants) {
        this.grabModuleConstants = grabModuleConstants;
    }

    @Override
    public Node visit(AssignmentExpression s, Map<String, VariableInfo> t) {
        if (!isVariable(s.target)) {
            return super.visit(s, t);
        }

        final String variableName = ((VariableExpression) s.target).name;
        final VariableInfo var = grabVariableInfo(t, variableName);

        if (s.operation == null && isValue(s.expression)) {
            var.value = ((ValueExpression) s.expression).value;
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(ForeachArrayStatement s, Map<String, VariableInfo> t) {
        grabVariableInfo(t, s.variable);
        return super.visit(s, t);
    }

    @Override
    public Node visit(ForeachMapStatement s, Map<String, VariableInfo> t) {
        grabVariableInfo(t, s.key);
        grabVariableInfo(t, s.value);
        return super.visit(s, t);
    }

    @Override
    public Node visit(MatchExpression s, Map<String, VariableInfo> t) {
        for (MatchExpression.Pattern pattern : s.patterns) {
            if (pattern instanceof MatchExpression.VariablePattern varPattern) {
                final String variableName = varPattern.variable;
                grabVariableInfo(t, variableName);
            }
        }
        return super.visit(s, t);
    }

    @Override
    public Node visit(UnaryExpression s, Map<String, VariableInfo> t) {
        if (s.expr instanceof Accessible) {
            if (s.expr instanceof VariableExpression varExpr) {
                grabVariableInfo(t, varExpr.name);
            }
            if (s.expr instanceof ContainerAccessExpression conExpr) {
                if (conExpr.rootIsVariable()) {
                    final String variableName = ((VariableExpression) conExpr.root).name;
                    grabVariableInfo(t, variableName);
                }
            }
        }
        return super.visit(s, t);
    }

    @Override
    protected boolean visit(Arguments in, Arguments out, Map<String, VariableInfo> t) {
        for (Argument argument : in) {
            final String variableName = argument.name();
            grabVariableInfo(t, variableName);
            /* No need to add value - it is optional arguments
            final Node expr = argument.getValueExpr();
            if (expr != null && isValue(expr)) {
                var.value = ((ValueExpression) expr).value;
            }*/
        }
        return super.visit(in, out, t);
    }



    private VariableInfo grabVariableInfo(Map<String, VariableInfo> t, final String variableName) {
        final VariableInfo var;
        if (t.containsKey(variableName)) {
            var = t.get(variableName);
            var.modifications++;
        } else {
            var = new VariableInfo();
            var.modifications = 1;
            t.put(variableName, var);
        }
        return var;
    }
}
