package model.statement;

import exceptions.InterpreterError;
import model.PrgState;
import model.adt.IStack;

public class CompoundStatement implements IStatement{
    private final IStatement first;
    private final IStatement second;

    public CompoundStatement(IStatement _first, IStatement _second) {
        first = _first;
        second = _second;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        IStack<IStatement> stack =  state.getExeStack();
        stack.push(second);
        stack.push(first);
        return state;
    }

    @Override
    public String toString() {
        return String.format("%s;\n%s", first, second);
    }
}
