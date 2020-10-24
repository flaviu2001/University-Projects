package model.types;

import exceptions.InterpreterError;
import model.values.IValue;

public interface IType {
    boolean equals(IType another);
    IValue getDefault();
}
