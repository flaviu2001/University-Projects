package model.statement;

import exceptions.InterpreterError;
import model.PrgState;
import model.adt.IDict;
import model.types.IType;
import model.values.IValue;

public class DeclarationStatement implements IStatement{
    private final String name;
    private final IType type;

    public DeclarationStatement(String _name, IType _type) {
        name = _name;
        type = _type;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        IDict<String, IValue> symTable = state.getSymTable();
        symTable.add(name, type.getDefault());
        return state;
    }

    @Override
    public String toString() {
        return String.format("Declaration{%s:%s}", name, type.toString());
    }
}
