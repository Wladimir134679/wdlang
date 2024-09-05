package ru.wdeath.lang.visitors.optimization;

import ru.wdeath.lang.ast.*;
import ru.wdeath.lang.lib.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.wdeath.lang.visitors.VisitorUtils.isValue;

public class OptimizationVisitor<T> implements ResultVisitor<Node, T> {

    @Override
    public Node visit(ArrayExpression s, T t) {
        final List<Expression> elements = new ArrayList<>(s.arguments.size());
        boolean changed = false;
        for (Expression expression : s.arguments) {
            final Node node = expression.accept(this, t);
            if (node != expression) {
                changed = true;
            }
            elements.add((Expression) node);
        }
        if (changed) {
            return new ArrayExpression(elements);
        }
        return s;
    }

    @Override
    public Node visit(AssignmentExpression s, T t) {
        final Node exprNode = s.expression.accept(this, t);
        final Node targetNode = s.target.accept(this, t);
        if ((exprNode != s.expression || targetNode != s.target) && (targetNode instanceof Accessible)) {
            return new AssignmentExpression(s.operation, (Accessible) targetNode, (Expression) exprNode);
        }
        return s;
    }

    @Override
    public Node visit(BinaryExpression s, T t) {
        final Node expr1 = s.expr1.accept(this, t);
        final Node expr2 = s.expr2.accept(this, t);
        if (expr1 != s.expr1 || expr2 != s.expr2) {
            return new BinaryExpression(s.operation, (Expression) expr1, (Expression) expr2);
        }
        return s;
    }

    @Override
    public Node visit(BlockStatement s, T t) {
        boolean changed = false;
        final BlockStatement result = new BlockStatement();
        for (Statement statement : s.statements) {
            final Node node = statement.accept(this, t);
            if (node != statement) {
                changed = true;
            }
            if (node instanceof Statement) {
                result.addStatement((Statement) node);
            } else if (node instanceof Expression) {
                result.addStatement(new ExprStatement((Expression) node));
            }
        }
        if (changed) {
            return result;
        }
        return s;
    }

    @Override
    public Node visit(BreakStatement s, T t) {
        return s;
    }

    @Override
    public Node visit(ConditionalExpression s, T t) {
        final Node expr1 = s.expr1.accept(this, t);
        final Node expr2 = s.expr2.accept(this, t);
        if (expr1 != s.expr1 || expr2 != s.expr2) {
            return new ConditionalExpression(s.operation, (Expression) expr1, (Expression) expr2);
        }
        return s;
    }

    @Override
    public Node visit(ContainerAccessExpression s, T t) {
        final Node root = s.root.accept(this, t);
        boolean changed = (root != s.root);
        final List<Expression> indices = new ArrayList<>(s.indexes.size());
        for (Expression expression : s.indexes) {
            final Node node = expression.accept(this, t);
            if (node != expression) {
                changed = true;
            }
            indices.add((Expression) node);
        }
        if (changed) {
            return new ContainerAccessExpression((Expression) root, indices);
        }
        return s;
    }

    @Override
    public Node visit(ContinueStatement s, T t) {
        return s;
    }

    @Override
    public Node visit(ClassDeclarationStatement s, T t) {
        return s;
    }

    @Override
    public Node visit(ObjectCreationExpression s, T t) {
        final List<Expression> args = new ArrayList<>();
        boolean changed = false;
        for (Expression argument : s.constructorArguments) {
            final Node expr = argument.accept(this, t);
            if (expr != argument) {
                changed = true;
            }
            args.add((Expression) expr);
        }

        if (changed) {
            return new ObjectCreationExpression(s.className, args);
        }
        return s;
    }

    @Override
    public Node visit(DoWhileStatement s, T t) {
        final Node condition = s.condition.accept(this, t);
        final Node statement = s.body.accept(this, t);
        if (condition != s.condition || statement != s.body) {
            return new DoWhileStatement((Expression) condition, consumeStatement(statement));
        }
        return s;
    }

    @Override
    public Node visit(ForStatement s, T t) {
        final Node initialization = s.init.accept(this, t);
        final Node termination = s.termination.accept(this, t);
        final Node increment = s.increment.accept(this, t);
        final Node statement = s.block.accept(this, t);
        if (initialization != s.init || termination != s.termination
                || increment != s.increment || statement != s.block) {
            return new ForStatement(consumeStatement(initialization),
                    (Expression) termination, consumeStatement(increment), consumeStatement(statement));
        }
        return s;
    }

    @Override
    public Node visit(ForeachArrayStatement s, T t) {
        final Node container = s.container.accept(this, t);
        final Node body = s.body.accept(this, t);
        if (container != s.container || body != s.body) {
            return new ForeachArrayStatement(s.variable, (Expression) container, consumeStatement(body));
        }
        return s;
    }

    @Override
    public Node visit(ForeachMapStatement s, T t) {
        final Node container = s.container.accept(this, t);
        final Node body = s.body.accept(this, t);
        if (container != s.container || body != s.body) {
            return new ForeachMapStatement(s.key, s.value, (Expression) container, consumeStatement(body));
        }
        return s;
    }

    @Override
    public Node visit(FunctionDefineStatement s, T t) {
        final Node body = s.body.accept(this, t);
        if (body != s.body) {
            return new FunctionDefineStatement(s.name, s.arguments, consumeStatement(body));
        }
        return s;
    }

    @Override
    public Node visit(FunctionReferenceExpression s, T t) {
        return s;
    }

    @Override
    public Node visit(ExprStatement s, T t) {
        final Node expr = s.expr.accept(this, t);
        if (expr != s.expr) {
            return new ExprStatement((Expression) expr);
        }
        return s;
    }

    @Override
    public Node visit(FunctionExpression s, T t) {
        final Node functionExpr = s.expression.accept(this, t);
        final FunctionExpression result = new FunctionExpression((Expression) functionExpr);
        boolean changed = functionExpr != s.expression;
        for (Expression argument : s.arguments) {
            final Node expr = argument.accept(this, t);
            if (expr != argument) {
                changed = true;
            }
            result.addArgument((Expression) expr);
        }
        if (changed) {
            return result;
        }
        return s;
    }

    @Override
    public Node visit(IfStatement s, T t) {
        final Node expression = s.condition.accept(this, t);
        final Node ifStatement = s.ifStatement.accept(this, t);
        final Node elseStatement;
        if (s.elseStatement != null) {
            elseStatement = s.elseStatement.accept(this, t);
        } else {
            elseStatement = null;
        }
        if (expression != s.condition || ifStatement != s.ifStatement || elseStatement != s.elseStatement) {
            return new IfStatement((Expression) expression, consumeStatement(ifStatement), consumeStatement(elseStatement));
        }
        return s;
    }

    @Override
    public Node visit(MapExpression s, T t) {
        final Map<Expression, Expression> elements = new HashMap<>(s.elements.size());
        boolean changed = false;
        for (Map.Entry<Expression, Expression> entry : s.elements.entrySet()) {
            final Node key = entry.getKey().accept(this, t);
            final Node value = entry.getValue().accept(this, t);
            if (key != entry.getKey() || value != entry.getValue()) {
                changed = true;
            }
            elements.put((Expression) key, (Expression) value);
        }
        if (changed) {
            return new MapExpression(elements);
        }
        return s;
    }

    @Override
    public Node visit(MatchExpression s, T t) {
        final Node expression = s.expression.accept(this, t);
        boolean changed = expression != s.expression;
        final List<MatchExpression.Pattern> patterns = new ArrayList<>(s.patterns.size());
        for (MatchExpression.Pattern pattern : s.patterns) {
            if (pattern instanceof MatchExpression.VariablePattern) {
                final String variable = ((MatchExpression.VariablePattern) pattern).variable;
                final VariableExpression expr = new VariableExpression(variable);
                final Node node = expr.accept(this, t);
                if ((node != expr) && isValue(node)) {
                    changed = true;
                    final Value value = ((ValueExpression) node).value;
                    final Expression optCondition = pattern.optCondition;
                    final Statement result = pattern.result;
                    pattern = new MatchExpression.ConstantPattern(value);
                    pattern.optCondition = optCondition;
                    pattern.result = result;
                }
            }

            final Node patternResult = pattern.result.accept(this, t);
            if (patternResult != pattern.result) {
                changed = true;
                pattern.result = consumeStatement(patternResult);
            }

            if (pattern.optCondition != null) {
                Node optCond = pattern.optCondition.accept(this, t);
                if (optCond != pattern.optCondition) {
                    changed = true;
                    pattern.optCondition = (Expression) optCond;
                }
            }

            patterns.add(pattern);
        }
        if (changed) {
            return new MatchExpression((Expression) expression, patterns);
        }
        return s;
    }

    @Override
    public Node visit(PrintStatement s, T t) {
        final Node expression = s.expression.accept(this, t);
        if (expression != s.expression) {
            return new PrintStatement((Expression) expression);
        }
        return s;
    }

    @Override
    public Node visit(ReturnStatement s, T t) {
        final Node expression = s.expression.accept(this, t);
        if (expression != s.expression) {
            return new ReturnStatement((Expression) expression);
        }
        return s;
    }

    @Override
    public Node visit(TernaryExpression s, T t) {
        final Node condition = s.condition.accept(this, t);
        final Node trueExpr = s.trueExpr.accept(this, t);
        final Node falseExpr = s.falseExpr.accept(this, t);
        if (condition != s.condition || trueExpr != s.trueExpr || falseExpr != s.falseExpr) {
            return new TernaryExpression((Expression) condition, (Expression) trueExpr, (Expression) falseExpr);
        }
        return s;
    }

    @Override
    public Node visit(UnaryExpression s, T t) {
        final Node expr1 = s.expr.accept(this, t);
        if (expr1 != s.expr) {
            return new UnaryExpression(s.operation, (Expression) expr1);
        }
        return s;
    }

    @Override
    public Node visit(ValueExpression s, T t) {
        return s;
    }

    @Override
    public Node visit(VariableExpression s, T t) {
        return s;
    }

    @Override
    public Node visit(WhileStatement s, T t) {
        final Node condition = s.condition.accept(this, t);
        final Node statement = s.body.accept(this, t);
        if (condition != s.condition || statement != s.body) {
            return new WhileStatement((Expression) condition, consumeStatement(statement));
        }
        return s;
    }

    protected Statement consumeStatement(Node node) {
        if (node instanceof Statement) {
            return (Statement) node;
        }
        return new ExprStatement((Expression) node);
    }
}
