package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.Type;
import model.values.Value;

public class AssignmentStatement implements Statement {
    private final String key;
    private final Expression expression;

    public AssignmentStatement(String _key, Expression _expression) {
        key = _key;
        expression = _expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        IDict<String, Value> symTable = state.getSymTable();
        Type type = symTable.get(key).getType();
        Value val = expression.eval(symTable, state.getHeap());
        if (!val.getType().equals(type))
            throw new InterpreterError(String.format("ERROR: %s is not compatible with %s", val.toString(), type.toString()));
        if (!symTable.containsKey(key))
            throw new InterpreterError(String.format("ERROR: %s does not exist in the symTable", key));
        symTable.put(key, val);
        return state;
    }

    @Override
    public String toString() {
        return String.format("Assignment{%s = %s}", key, expression);
    }
}
