package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.Type;

public class PrintStatement implements Statement {
    private final Expression expression;

    public PrintStatement(Expression _expression) {
        expression = _expression;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        expression.typeCheck(typeTable);
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        state.getOut().add(
                expression.eval(state.getSymTable(), state.getHeap())
                           .toString()
        );
        return null;
    }

    @Override
    public String toString() {
        return String.format("Print{%s}", expression.toString());
    }
}
