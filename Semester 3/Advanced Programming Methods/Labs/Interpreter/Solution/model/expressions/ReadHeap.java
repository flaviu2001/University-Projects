package model.expressions;

import exceptions.InterpreterError;
import model.adt.IDict;
import model.values.ReferenceValue;
import model.values.Value;

public class ReadHeap implements Expression {
    private final Expression expression;

    public ReadHeap(Expression _expression) {
        expression = _expression;
    }

    @Override
    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) throws InterpreterError {
        Value evaluated = expression.eval(symTable, heap);
        if (!(evaluated instanceof ReferenceValue))
            throw new InterpreterError(String.format("ERROR: %s not of ReferenceType", evaluated));
        ReferenceValue referenceValue = (ReferenceValue)evaluated;
        if (!heap.containsKey(referenceValue.getAddress()))
            throw new InterpreterError(String.format("ERROR: %s not present in the heap", referenceValue.getAddress()));
        return heap.get(referenceValue.getAddress());
    }

    @Override
    public String toString() {
        return String.format("ReadHeap{%s}", expression);
    }
}
