package model.values;

import model.types.BoolType;
import model.types.Type;

public class BoolValue implements Value {
    private final boolean val;

    public BoolValue(boolean _val) {
        val = _val;
    }

    public boolean getVal() {
        return val;
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return val ? "true" : "false";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoolValue))
            return false;
        BoolValue castObj = (BoolValue)obj;
        return val == castObj.val;
    }
}
