package model.statement;

import exceptions.InterpreterError;
import model.PrgState;
import model.exp.IExpression;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class IfStatement implements IStatement{
    private final IExpression exp;
    private final IStatement first;
    private final IStatement second;

    public IfStatement(IExpression _exp, IStatement _first, IStatement _second) {
        exp = _exp;
        first = _first;
        second = _second;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterError {
        IValue value = exp.eval(state.getSymTable());
        if (value.getType().equals(new BoolType())) {
            BoolValue condition = (BoolValue)value;
            if (condition.getVal())
                state.getExeStack().push(first);
            else state.getExeStack().push(second);
            return state;
        }
        throw new InterpreterError(String.format("ERROR: %s not of type bool inside if", value.toString()));
    }

    @Override
    public String toString() {
        return String.format("if(%s){\n\t%s\n}else{\n\t%s\n}", exp.toString(), first.toString(), second.toString());
    }
}
