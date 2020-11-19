package model.statements;

import model.ProgramState;

public class NopStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public String toString() {
        return "NopStatement";
    }
}
