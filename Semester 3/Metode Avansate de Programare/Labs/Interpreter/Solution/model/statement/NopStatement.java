package model.statement;

import exceptions.InterpreterError;
import model.PrgState;

public class NopStatement implements IStatement {
    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        return state;
    }

    @Override
    public String toString() {
        return "NopStatement";
    }
}
