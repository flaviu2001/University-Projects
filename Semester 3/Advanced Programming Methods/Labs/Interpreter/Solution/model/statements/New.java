package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.adt.IHeap;
import model.expressions.Expression;
import model.types.ReferenceType;
import model.types.Type;
import model.values.ReferenceValue;
import model.values.Value;

public class New implements Statement {
    private final String variableName;
    private final Expression expression;

    public New(String _varName, Expression _expression) {
        variableName = _varName;
        expression = _expression;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        Type typeVariable = typeTable.get(variableName);
        Type typeExpression = expression.typeCheck(typeTable);
        if (!typeVariable.equals(new ReferenceType(typeExpression)))
            throw new InterpreterError("New: right hand side and left hand side have different types ");
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        IDict<String, Value> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symTable.containsKey(variableName))
            throw new InterpreterError(String.format("ERROR: %s not in symTable", variableName));
        Value varValue = symTable.get(variableName);
        if (!(varValue.getType() instanceof ReferenceType))
            throw new InterpreterError(String.format("ERROR: %s not of ReferenceType", variableName));
        Value evaluated = expression.eval(symTable, heap);
        Type locationType = ((ReferenceValue)varValue).getLocationType();
        if (!locationType.equals(evaluated.getType()))
            throw new InterpreterError(String.format("ERROR: %s not of %s", variableName, evaluated.getType()));
        Integer newPosition = heap.add(evaluated);
        symTable.put(variableName, new ReferenceValue(newPosition, locationType)); // Update symTable
        return null;
    }

    @Override
    public String toString() {
        return String.format("New{%s, %s}", variableName, expression);
    }
}
