package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;

public interface Statement {
    ProgramState execute(ProgramState state) throws InterpreterError;
}
