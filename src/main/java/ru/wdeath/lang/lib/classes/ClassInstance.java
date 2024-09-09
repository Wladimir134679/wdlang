package ru.wdeath.lang.lib.classes;

import ru.wdeath.lang.exception.TypeException;
import ru.wdeath.lang.exception.WdlRuntimeException;
import ru.wdeath.lang.lib.*;

import java.util.Objects;

public class ClassInstance implements Value {

    private final String className;
    private final MapValue thisMap;
    private ClassMethod constructor;
    private ClassMethod toString;
    private boolean isInstantiated;

    public ClassInstance(String name) {
        this.className = name;
        thisMap = new MapValue(10);
    }

    public MapValue getThisMap() {
        return thisMap;
    }

    public String getClassName() {
        return className;
    }

    public void addField(ClassField f) {
        thisMap.set(f.name(), f.evaluableValue().eval());
    }

    public void addMethod(ClassMethod method) {
        method.setClassInstance(this);
        final String name = method.getName();
        thisMap.set(name, method);
        if (name.equals(className)) {
            constructor = method;
        } else if (name.equals("toString")) {
            toString = method;
        }
    }

    public ClassInstance callConstructor(Value[] args) {
        if (isInstantiated) {
            throw new WdlRuntimeException(
                    "Class %s was already instantiated".formatted(className));
        }
        if (constructor != null) {
            CallStack.enter("class " + className, constructor, null);
            constructor.execute(args);
            CallStack.exit();
        }
        isInstantiated = true;
        return this;
    }

    public Value access(Value value) {
        return thisMap.get(value);
    }

    public void set(Value key, Value value) {
        final Value v = thisMap.get(key);
        if (v == null) {
            throw new WdlRuntimeException(
                    "Unable to add new field %s to class %s"
                            .formatted(key.asString(), className));
        }
        thisMap.set(key, value);
    }

    @Override
    public Object asJavaObject() {
        return thisMap.asJavaObject();
    }

    @Override
    public Object raw() {
        return thisMap;
    }

    @Override
    public int asInt() {
        throw new TypeException("Cannot cast class " + className + " to integer");
    }

    @Override
    public double asDouble() {
        throw new TypeException("Cannot cast class " + className + " to number");
    }

    @Override
    public String asString() {
        Value toString = thisMap.get(new StringValue("toString"));
        if (toString != null) {
            if (toString instanceof ClassMethod cm) {
                return cm.execute(new Value[0]).asString();
            }
        }
        return className + "@" + thisMap;
    }

    @Override
    public int type() {
        return Types.CLASS;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hash(className, thisMap);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final ClassInstance other = (ClassInstance) obj;
        return Objects.equals(this.className, other.className)
                && Objects.equals(this.thisMap, other.thisMap);
    }

    @Override
    public int compareTo(Value o) {
        return asString().compareTo(o.asString());
    }

    @Override
    public String toString() {
        return asString();
    }
}
