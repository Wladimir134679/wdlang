package ru.wdeath.lang.visitors;

import ru.wdeath.lang.ast.*;
import ru.wdeath.lang.lib.FunctionValue;
import ru.wdeath.lang.lib.Types;
import ru.wdeath.lang.lib.UserDefinedFunction;
import ru.wdeath.lang.lib.Value;

public abstract class AbstractVisitor implements Visitor {


    @Override
    public void visit(Argument st) {
        if (st.valueExpr() != null)
            st.valueExpr().accept(this);
    }

    @Override
    public void visit(ImportStatement st) {
    }

    @Override
    public void visit(ExpansionStatement st) {

    }

    @Override
    public void visit(AssignmentExpression st) {
        st.target.accept(this);
        st.expression.accept(this);
    }

    @Override
    public void visit(FunctionDefineStatement st) {
        st.body.accept(this);
        st.arguments.forEach(s -> s.accept(this));
    }

    @Override
    public void visit(ExprStatement st) {
        st.expr.accept(this);
    }

    @Override
    public void visit(ArrayExpression st) {
        for (Node argument : st.arguments) {
            argument.accept(this);
        }
    }

    @Override
    public void visit(ContainerAccessExpression st) {
        st.root.accept(this);
        st.indexes.forEach(index -> index.accept(this));
    }

    @Override
    public void visit(BinaryExpression st) {
        st.expr1.accept(this);
        st.expr2.accept(this);
    }

    @Override
    public void visit(BlockStatement st) {
        st.statements.forEach(s -> s.accept(this));
    }

    @Override
    public void visit(BreakStatement st) {

    }

    @Override
    public void visit(ConditionalExpression st) {
        st.expr1.accept(this);
        st.expr2.accept(this);

    }

    @Override
    public void visit(ContinueStatement st) {

    }


    @Override
    public void visit(ClassDeclarationStatement st) {
        for (Node field : st.fields) {
            field.accept(this);
        }
        for (Node method : st.methods) {
            method.accept(this);
        }
    }

    @Override
    public void visit(DoWhileStatement st) {
        st.condition.accept(this);
        st.body.accept(this);
    }

    @Override
    public void visit(ForStatement st) {
        st.init.accept(this);
        st.termination.accept(this);
        st.increment.accept(this);
        st.block.accept(this);
    }

    @Override
    public void visit(FunctionExpression st) {
        st.arguments.forEach(s -> s.accept(this));
        st.expression.accept(this);
    }

    @Override
    public void visit(IfStatement st) {
        st.condition.accept(this);
        st.ifStatement.accept(this);
        if (st.elseStatement != null) {
            st.elseStatement.accept(this);
        }
    }

    @Override
    public void visit(PrintStatement st) {
        st.expression.accept(this);
    }

    @Override
    public void visit(ReturnStatement st) {
        st.expression.accept(this);
    }

    @Override
    public void visit(UnaryExpression st) {
        st.expr.accept(this);
    }

    @Override
    public void visit(ValueExpression st) {
        if (st.value instanceof FunctionValue fv &&
                fv.getFunction() instanceof UserDefinedFunction udf) {
            visit(udf);
        }
    }

    @Override
    public void visit(VariableExpression st) {

    }

    @Override
    public void visit(WhileStatement st) {
        st.condition.accept(this);
        st.body.accept(this);
    }

    @Override
    public void visit(TernaryExpression st) {
        st.condition.accept(this);
        st.trueExpr.accept(this);
        st.falseExpr.accept(this);
    }

    @Override
    public void visit(MapExpression st) {
        for (Node key : st.elements.keySet()) {
            key.accept(this);
            st.elements.get(key).accept(this);
        }
    }

    @Override
    public void visit(FunctionReferenceExpression st) {
    }

    @Override
    public void visit(ForeachArrayStatement st) {
        st.container.accept(this);
        st.body.accept(this);
    }

    @Override
    public void visit(ForeachMapStatement st) {
        st.container.accept(this);
        st.body.accept(this);
    }

    @Override
    public void visit(MatchExpression st) {
        st.expression.accept(this);
        st.patterns.forEach(pattern -> {
            if (pattern.optCondition != null)
                pattern.optCondition.accept(this);
            pattern.result.accept(this);
        });
    }

    @Override
    public void visit(ObjectCreationExpression st) {
        for (Node argument : st.constructorArguments) {
            argument.accept(this);
        }
    }

    public void visit(UserDefinedFunction userDefinedFunction) {
        userDefinedFunction.body.accept(this);
        userDefinedFunction.arguments.forEach(argument -> argument.accept(this));
    }
}