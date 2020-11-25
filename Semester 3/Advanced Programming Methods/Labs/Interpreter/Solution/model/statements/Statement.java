package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.types.Type;

public interface Statement {
    ProgramState execute(ProgramState state) throws InterpreterError;
    IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError;
}
