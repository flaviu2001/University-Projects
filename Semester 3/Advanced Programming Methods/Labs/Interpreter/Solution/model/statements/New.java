package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.ReferenceType;
import model.types.Type;
import model.values.ReferenceValue;
import model.values.Value;

import java.util.Random;

public class New implements Statement {
    private final String varName;
    private final Expression expression;

    public New(String _varName, Expression _expression) {
        varName = _varName;
        expression = _expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        IDict<String, Value> symTable = state.getSymTable();
        IDict<Integer, Value> heap = state.getHeap();
        if (!symTable.containsKey(varName))
            throw new InterpreterError(String.format("ERROR: %s not in symTable", varName));
        Value varValue = symTable.get(varName);
        if (!(varValue.getType() instanceof ReferenceType))
            throw new InterpreterError(String.format("ERROR: %s not of ReferenceType", varName));
        Value evaluated = expression.eval(symTable, heap);
        Type locationType = ((ReferenceValue)varValue).getLocationType();
        if (!locationType.equals(evaluated.getType()))
            throw new InterpreterError(String.format("ERROR: %s not of %s", varName, evaluated.getType()));
        Random rand = new Random();
        int newPosition = rand.nextInt();
        if (newPosition == 0 || heap.containsKey(newPosition))
            newPosition = rand.nextInt();
        heap.put(newPosition, evaluated);
        symTable.put(varName, new ReferenceValue(newPosition, locationType)); // Update symTable
        return state;
    }

    @Override
    public String toString() {
        return String.format("New{%s, %s}", varName, expression);
    }
}
