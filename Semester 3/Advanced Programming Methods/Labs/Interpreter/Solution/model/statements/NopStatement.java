package model.statements;

import model.ProgramState;
import model.adt.IDict;
import model.types.Type;

public class NopStatement implements Statement {
    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) {
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public String toString() {
        return "NopStatement";
    }
}
