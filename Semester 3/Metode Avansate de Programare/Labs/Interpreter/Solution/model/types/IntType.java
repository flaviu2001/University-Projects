package model.types;

import exceptions.InterpreterError;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class IntType implements IType {
    @Override
    public boolean equals(IType another) {
        return another instanceof IntType;
    }

    @Override
    public IValue getDefault() {
        return new IntValue(0);
    }

    @Override
    public String toString() {
        return "int";
    }
}
