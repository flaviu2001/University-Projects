package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IStack;
import model.adt.Stack;

public class Fork implements Statement {
    Statement statement;

    public Fork(Statement _statement) {
        statement = _statement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IStack<Statement> newExeStack = new Stack<>();
        newExeStack.push(statement);
        return new ProgramState(newExeStack, state.getSymTable().copy(),
                state.getOut(), state.getFileTable(), state.getHeap());
    }

    @Override
    public String toString() {
        return String.format("Fork{\n%s\n}", statement);
    }
}
