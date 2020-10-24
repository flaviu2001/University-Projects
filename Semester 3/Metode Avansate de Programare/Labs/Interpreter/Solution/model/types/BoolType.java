package model.types;

import exceptions.InterpreterError;
import model.values.BoolValue;
import model.values.IValue;

public class BoolType implements IType{
    @Override
    public boolean equals(IType another) {
        return another instanceof BoolType;
    }

    @Override
    public IValue getDefault() {
        return new BoolValue(false);
    }

    @Override
    public String toString() {
        return "bool";
    }
}
