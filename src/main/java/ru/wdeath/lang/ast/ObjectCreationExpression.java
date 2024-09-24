package ru.wdeath.lang.ast;

import ru.wdeath.lang.ProgramContext;
import ru.wdeath.lang.exception.UnknownClassException;
import ru.wdeath.lang.lib.*;
import ru.wdeath.lang.lib.classes.ClassDeclaration;
import ru.wdeath.lang.utils.Range;
import ru.wdeath.lang.utils.SourceLocation;

import java.util.Iterator;
import java.util.List;

public class ObjectCreationExpression implements Node, SourceLocation {

    public final ProgramContext programContext;
    public final List<String> classNames;
    public final List<Node> constructorArguments;
    private Range range;

    public ObjectCreationExpression(ProgramContext programContext, List<String> classNames, List<Node> constructorArguments, Range range) {
        this.programContext = programContext;
        this.classNames = classNames;
        this.constructorArguments = constructorArguments;
        this.range = range;
    }

    @Override
    public Value eval() {
        final ClassDeclaration cd = getDeclaration();
        if (cd != null) {
            return cd.newInstance(constructorArgs(), programContext);
        }
        throw new UnknownClassException(String.join(",", classNames), range);

        // Legacy code ownLang
//        // Is Instantiable?
//        if (programContext.getScope().isVariableOrConstantExists(className)) {
//            final Value variable = programContext.getScope().getVariableOrConstant(className);
//            if (variable instanceof Instantiable instantiable) {
//                return instantiable.newInstance(constructorArgs(), programContext);
//            }
//        }
    }

    private ClassDeclaration getDeclaration() {
        ProgramContext context = this.programContext;
        final var last = classNames.size() - 1;
        for (int i = 0; i < last; i++) {
            Value variable = context.getScope().getVariable(classNames.get(i));
            if (variable.type() == Types.IMPORT) {
                context = ((ImportValue) variable).context;
            } else {
                throw new UnknownClassException(String.join(",", classNames), range);
            }
        }

        return context.getScope().getClassDeclaration(classNames.get(last));
    }


    private Value[] constructorArgs() {
        final int argsSize = constructorArguments.size();
        final Value[] args = new Value[argsSize];
        int i = 0;
        for (Node argument : constructorArguments) {
            args[i++] = argument.eval();
        }
        return args;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    @Override
    public Range getRange() {
        return range;
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
        final StringBuilder sb = new StringBuilder();
        sb.append("new ").append(classNames).append(' ');
        final Iterator<Node> it = constructorArguments.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(", ").append(it.next());
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
