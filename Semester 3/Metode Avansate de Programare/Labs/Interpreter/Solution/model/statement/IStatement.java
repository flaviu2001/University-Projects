package model.statement;

import exceptions.InterpreterError;
import model.PrgState;

public interface IStatement {
    PrgState execute(PrgState state) throws InterpreterError;
}
