package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.expressions.Expression;

public class PrintStatement implements Statement {
    private final Expression expression;

    public PrintStatement(Expression _expression) {
        expression = _expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        state.getOut().add(
                expression.eval(state.getSymTable())
                           .toString()
        );
        return state;
    }

    @Override
    public String toString() {
        return String.format("Print{%s}", expression.toString());
    }
}
