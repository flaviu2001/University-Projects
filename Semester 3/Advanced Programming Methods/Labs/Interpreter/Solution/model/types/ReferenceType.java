package model.types;

import model.values.ReferenceValue;
import model.values.Value;

public class ReferenceType implements Type{
    private final Type inner;

    public ReferenceType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public boolean equals(Type another){
        if (another instanceof ReferenceType)
            return inner.equals(((ReferenceType)another).getInner());
        else
            return false;
    }

    @Override
    public String toString() {
        return "Reference(" +inner.toString()+")";
    }

    @Override
    public Value getDefault() {
        return new ReferenceValue(0,inner);
    }
}
