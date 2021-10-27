package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class IfStatement implements Statement {
    private final Expression exp;
    private final Statement first;
    private final Statement second;

    public IfStatement(Expression _exp, Statement _first, Statement _second) {
        exp = _exp;
        first = _first;
        second = _second;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        if (!exp.typeCheck(typeTable).equals(new BoolType()))
            throw new InterpreterError("The condition of If doesn't have the type bool");
        first.typeCheck(typeTable.copy());
        second.typeCheck(typeTable.copy());
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        Value value = exp.eval(state.getSymTable(), state.getHeap());
        if (value.getType().equals(new BoolType())) {
            BoolValue condition = (BoolValue)value;
            if (condition.getVal())
                state.getExecutionStack().push(first);
            else state.getExecutionStack().push(second);
            return null;
        }
        throw new InterpreterError(String.format("ERROR: %s not of type bool inside if", value.toString()));
    }

    @Override
    public String toString() {
        return String.format("if(%s){\n\t%s\n}else{\n\t%s\n}", exp.toString(), first.toString(), second.toString());
    }
}
