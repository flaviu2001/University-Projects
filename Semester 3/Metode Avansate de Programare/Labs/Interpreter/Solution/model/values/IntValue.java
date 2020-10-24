package model.values;

import model.types.IType;
import model.types.IntType;

public class IntValue implements IValue{
    int val;

    public IntValue(int _val) {
        val = _val;
    }

    public int getVal() {
        return val;
    }

    @Override
    public IType getType() {
        return new IntType();
    }

    @Override
    public String toString() {
        return String.format("%d", val);
    }
}
