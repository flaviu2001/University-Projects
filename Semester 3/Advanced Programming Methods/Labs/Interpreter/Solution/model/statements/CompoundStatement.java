package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.adt.IStack;
import model.types.Type;

public class CompoundStatement implements Statement {
    private final Statement first;
    private final Statement second;

    public CompoundStatement(Statement _first, Statement _second) {
        first = _first;
        second = _second;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        return second.typeCheck(first.typeCheck(typeTable));
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IStack<Statement> stack =  state.getExecutionStack();
        stack.push(second);
        stack.push(first);
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s;\n%s", first, second);
    }
}
