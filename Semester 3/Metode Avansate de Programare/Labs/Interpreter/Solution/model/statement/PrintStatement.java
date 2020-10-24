package model.statement;

import exceptions.InterpreterError;
import model.PrgState;
import model.exp.IExpression;

public class PrintStatement implements IStatement {
    IExpression expression;

    public PrintStatement(IExpression _expression) {
        expression = _expression;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        state.getOut().add(expression.eval(state.getSymTable()).toString());
        return state;
    }

    @Override
    public String toString() {
        return String.format("Print{%s}", expression.toString());
    }
}
