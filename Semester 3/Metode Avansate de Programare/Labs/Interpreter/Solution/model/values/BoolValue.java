package model.values;

import model.types.BoolType;
import model.types.IType;

public class BoolValue implements IValue {
    boolean val;

    public BoolValue(boolean _val) {
        val = _val;
    }

    public boolean getVal() {
        return val;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return val ? "true" : "false";
    }
}
