package model.values;

import model.types.Type;
import model.types.IntType;

public class IntValue implements Value {
    private final int val;

    public IntValue(int _val) {
        val = _val;
    }

    public int getVal() {
        return val;
    }

    @Override
    public Type getType() {
        return new IntType();
    }

    @Override
    public String toString() {
        return String.format("%d", val);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntValue))
            return false;
        IntValue castObj = (IntValue) obj;
        return val == castObj.val;
    }
}
