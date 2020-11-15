package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.adt.IHeap;
import model.expressions.Expression;
import model.values.ReferenceValue;
import model.values.Value;

public class WriteHeap implements Statement {
    private final String varName;
    private final Expression expression;

    public WriteHeap(String _varName, Expression _expression) {
        varName = _varName;
        expression = _expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        IDict<String, Value> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symTable.containsKey(varName))
            throw new InterpreterError(String.format("ERROR: %s not present in the symTable", varName));
        Value varValue = symTable.get(varName);
        if (!(varValue instanceof ReferenceValue))
            throw new InterpreterError(String.format("ERROR: %s not of ReferenceType", varValue));
        ReferenceValue referenceValue = (ReferenceValue)varValue;
        Value evaluated = expression.eval(symTable, heap);
        if (!evaluated.getType().equals(referenceValue.getLocationType()))
            throw new InterpreterError(String.format("ERROR: %s not of %s", evaluated, referenceValue.getLocationType()));
        heap.update(referenceValue.getAddress(), evaluated);
        return state;
    }

    @Override
    public String toString() {
        return String.format("WriteHeap{%s, %s}", varName, expression);
    }
}
