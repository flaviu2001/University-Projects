package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.adt.IStack;
import model.adt.Stack;
import model.types.Type;

public class Fork implements Statement {
    private final Statement statement;

    public Fork(Statement _statement) {
        statement = _statement;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        statement.typeCheck(typeTable.copy());
        return typeTable;
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
