package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.expressions.Expression;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.Value;

public class WhileStatement implements Statement {
    private final Expression expression;
    private final Statement statement;

    public WhileStatement(Expression _expression, Statement _statement) {
        expression = _expression;
        statement = _statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        Value value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new BoolType()))
            throw new InterpreterError(String.format("ERROR: %s is not of BoolType", value));
        BoolValue boolValue = (BoolValue) value;
        if (boolValue.getVal()) {
            state.getExeStack().push(this);
            state.getExeStack().push(statement);
        }
        return state;
    }

    @Override
    public String toString() {
        return String.format("While(%s){\n%s}", expression, statement);
    }
}
