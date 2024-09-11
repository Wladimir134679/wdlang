package ru.wdeath.lang.visitors;

import ru.wdeath.lang.ast.*;
import ru.wdeath.lang.lib.FunctionValue;
import ru.wdeath.lang.lib.UserDefinedFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MermaidchartVisitor extends AbstractVisitor {

    public static int INDEX = 0;

    private static class MermaidLine {
        public String subname1;
        public String subname2;

        public MermaidLine(Node node1) {
            this.subname1 = node1.getClass().getSimpleName() + hash(node1) + "(\"" + node1.toString().replace("\"", "'").replace("\n", "") + "\")";
        }

        public MermaidLine(Node node1, Node node2) {
            this.subname1 = node1.getClass().getSimpleName() + hash(node1) + "(\"" + node1.toString().replace("\"", "'").replace("\n", "") + "\")";
            this.subname2 = node2.getClass().getSimpleName() + hash(node2) + "(\"" + node2.toString().replace("\"", "'").replace("\n", "") + "\")";
//            this.subname2 = node2.toString();
        }

        public String hash(Node n) {
            return String.valueOf(n.hashCode());
        }

        public String index() {
            INDEX++;
            return String.valueOf(INDEX);
        }

        public MermaidLine(String subname1, String subname2) {
            this.subname1 = subname1;
            this.subname2 = subname2;
        }

        public String format() {
            if (subname2 != null)
                return subname1 + " --> " + subname2;
            return subname1;
//            return "\t\"" + subname1 + "\" --> \"" + subname2 + "\"";
//            return "\t\"" + subname1 + "\" --> \"" + subname2 + "\"";
        }
    }


    public final List<MermaidLine> lines = new ArrayList<MermaidLine>();

    public void print() {
        for (MermaidLine line : lines) {
            System.out.println(line.format());
        }
    }

    public void add(Node n1, Node n2) {
        if (n2 == null)
            add(n1);
        else
            lines.add(new MermaidLine(n1, n2));
    }


    public void add(Node n1) {
        lines.add(new MermaidLine(n1));
    }

    @Override
    public void visit(AssignmentExpression st) {
        add(st, st.target);
        add(st, st.expression);
        super.visit(st);
    }

    @Override
    public void visit(FunctionDefineStatement st) {
        add(st, st.body);
        st.arguments.forEach(argument -> add(st, argument));
        super.visit(st);
    }

    @Override
    public void visit(ExprStatement st) {
        add(st, st.expr);
        super.visit(st);
    }

    @Override
    public void visit(ArrayExpression st) {
        st.arguments.forEach(argument -> add(st, argument));
        super.visit(st);
    }

    @Override
    public void visit(ContainerAccessExpression st) {
        add(st, st.root);
        st.indexes.forEach(index -> add(st, index));
        super.visit(st);
    }

    @Override
    public void visit(BinaryExpression st) {
        add(st, st.expr1);
        add(st, st.expr2);
        super.visit(st);
    }

    @Override
    public void visit(BlockStatement st) {
        st.statements.forEach(statement -> add(st, statement));
        super.visit(st);
    }

    @Override
    public void visit(BreakStatement st) {
        add(st);
        super.visit(st);
    }

    @Override
    public void visit(ConditionalExpression st) {
        add(st, st.expr1);
        add(st, st.expr2);
        super.visit(st);
    }

    @Override
    public void visit(ContinueStatement st) {
        add(st);
        super.visit(st);
    }

    @Override
    public void visit(DoWhileStatement st) {
        add(st, st.condition);
        add(st, st.body);
        super.visit(st);
    }

    @Override
    public void visit(ForStatement st) {
        add(st, st.init);
        add(st, st.termination);
        add(st, st.increment);
        add(st, st.block);
        super.visit(st);
    }

    @Override
    public void visit(FunctionExpression st) {
        add(st, st.expression);
        st.arguments.forEach(argument -> add(st, argument));
        super.visit(st);
    }

    @Override
    public void visit(IfStatement st) {
        add(st, st.condition);
        add(st, st.ifStatement);
        add(st, st.elseStatement);
        super.visit(st);
    }

    @Override
    public void visit(PrintStatement st) {
        add(st, st.expression);
        super.visit(st);
    }

    @Override
    public void visit(ReturnStatement st) {
        add(st, st.expression);
        super.visit(st);
    }

    @Override
    public void visit(UnaryExpression st) {
        add(st, st.expr);
        super.visit(st);
    }

    @Override
    public void visit(ValueExpression st) {
        if (st.value instanceof FunctionValue fv &&
                fv.getFunction() instanceof UserDefinedFunction udf) {
            add(st, udf.body);
            udf.arguments.forEach(argument -> add(st, argument));
        }
        super.visit(st);
    }

    @Override
    public void visit(VariableExpression st) {
        add(st);
        super.visit(st);
    }

    @Override
    public void visit(WhileStatement st) {
        add(st, st.condition);
        add(st, st.body);
        super.visit(st);
    }

    @Override
    public void visit(TernaryExpression st) {
        add(st, st.condition);
        add(st, st.trueExpr);
        add(st, st.falseExpr);
        super.visit(st);
    }

    @Override
    public void visit(MapExpression st) {
        st.elements.forEach((node, node2) -> {
            add(st, node);
            add(st, node2);
        });
        super.visit(st);
    }

    @Override
    public void visit(FunctionReferenceExpression st) {
        add(st);
        super.visit(st);
    }

    @Override
    public void visit(ForeachArrayStatement st) {
        add(st, st.container);
        add(st, st.body);
        super.visit(st);
    }

    @Override
    public void visit(ForeachMapStatement st) {
        add(st, st.container);
        add(st, st.body);
        super.visit(st);
    }

    @Override
    public void visit(MatchExpression st) {
        add(st.expression);
        st.patterns.forEach(pattern -> {
            add(st, pattern.optCondition);
            add(pattern.optCondition, pattern.result);
        });
        super.visit(st);
    }

    @Override
    public void visit(ObjectCreationExpression st) {
        st.constructorArguments.forEach(node -> add(st, node));
        super.visit(st);
    }

    @Override
    public void visit(ClassDeclarationStatement st) {
        st.methods.forEach(method -> add(st, method));
        st.fields.forEach(f -> add(st, f));
        super.visit(st);
    }

    @Override
    public void visit(Argument st) {
        if (st.valueExpr() != null)
            add(st, st.valueExpr());
        super.visit(st);
    }

}
