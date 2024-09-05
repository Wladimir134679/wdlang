package ru.wdeath.lang.ast;

public interface Visitor {

    void visit(AssignmentExpression st);

    void visit(FunctionDefineStatement st);

    void visit(ExprStatement st);

    void visit(ArrayExpression st);

    void visit(ContainerAccessExpression st);

    void visit(BinaryExpression st);

    void visit(BlockStatement st);

    void visit(BreakStatement st);

    void visit(ConditionalExpression st);

    void visit(ContinueStatement st);

    void visit(DoWhileStatement st);

    void visit(ForStatement st);

    void visit(FunctionExpression st);

    void visit(IfStatement st);

    void visit(PrintStatement st);

    void visit(ReturnStatement st);

    void visit(UnaryExpression st);

    void visit(ValueExpression st);

    void visit(VariableExpression st);

    void visit(WhileStatement st);

    void visit(TernaryExpression st);

    void visit(MapExpression st);

    void visit(FunctionReferenceExpression st);

    void visit(ForeachArrayStatement st);

    void visit(ForeachMapStatement st);

    void visit(MatchExpression st);

    void visit(ObjectCreationExpression st);

    void visit(ClassDeclarationStatement st);
}
