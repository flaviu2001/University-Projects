package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.types.Type;
import model.values.Value;

public class DeclarationStatement implements Statement {
    private final String name;
    private final Type type;

    public DeclarationStatement(String _name, Type _type) {
        name = _name;
        type = _type;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) {
        typeTable.put(name, type);
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        IDict<String, Value> symTable = state.getSymTable();
        if (symTable.containsKey(name))
            throw new InterpreterError(String.format("ERROR: %s already exists in the symTable", name));
        symTable.put(name, type.getDefault());
        return null;
    }

    @Override
    public String toString() {
        return String.format("Declaration{%s:%s}", name, type.toString());
    }
}
