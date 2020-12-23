package model.expressions;

import exceptions.InterpreterError;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.ReferenceType;
import model.types.Type;
import model.values.ReferenceValue;
import model.values.Value;

public class ReadHeap implements Expression {
    private final Expression expression;

    public ReadHeap(Expression _expression) {
        expression = _expression;
    }

    @Override
    public Type typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        Type type = expression.typeCheck(typeTable);
        if (type instanceof ReferenceType) {
            ReferenceType reference = (ReferenceType) type;
            return reference.getInner();
        } else
            throw new InterpreterError("the ReadHeap argument is not a Reference Type");
    }

    @Override
    public Value eval(IDict<String, Value> symbolTable, IHeap heap) throws InterpreterError {
        Value evaluated = expression.eval(symbolTable, heap);
        if (!(evaluated instanceof ReferenceValue))
            throw new InterpreterError(String.format("ERROR: %s not of ReferenceType", evaluated));
        ReferenceValue referenceValue = (ReferenceValue)evaluated;
        return heap.get(referenceValue.getAddress());
    }

    @Override
    public String toString() {
        return String.format("ReadHeap{%s}", expression);
    }
}
