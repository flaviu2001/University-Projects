package model.types;

import model.values.Value;
import model.values.IntValue;

public class IntType implements Type {
    @Override
    public boolean equals(Type another) {
        return another instanceof IntType;
    }

    @Override
    public Value getDefault() {
        return new IntValue(0);
    }

    @Override
    public String toString() {
        return "int";
    }
}
