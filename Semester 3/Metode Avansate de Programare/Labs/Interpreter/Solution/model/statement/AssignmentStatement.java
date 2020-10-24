package model.statement;

import exceptions.InterpreterError;
import model.PrgState;
import model.adt.IDict;
import model.exp.IExpression;
import model.types.IType;
import model.values.IValue;

public class AssignmentStatement implements IStatement{
    private final String key;
    private final IExpression expression;

    public AssignmentStatement(String _key, IExpression _expression) {
        key = _key;
        expression = _expression;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        IDict<String, IValue> symTable = state.getSymTable();
        IType type = symTable.lookup(key).getType();
        IValue val = expression.eval(symTable);
        if (!val.getType().equals(type))
            throw new InterpreterError(String.format("ERROR: %s is not compatible with %s", val.toString(), type.toString()));
        symTable.update(key, val);
        return state;
    }

    @Override
    public String toString() {
        return String.format("Assignment{%s = %s}", key, expression.toString());
    }
}
