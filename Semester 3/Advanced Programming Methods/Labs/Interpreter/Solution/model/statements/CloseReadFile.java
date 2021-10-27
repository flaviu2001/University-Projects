package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.StringType;
import model.types.Type;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFile implements Statement {
    private final Expression expression;

    public CloseReadFile(Expression _expression) {
        expression = _expression;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        if (!expression.typeCheck(typeTable).equals(new StringType()))
            throw new InterpreterError("ERROR: CloseReadFile requires a string expression");
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        Value value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new StringType()))
            throw new InterpreterError(String.format("ERROR: %s does not evaluate to StringValue", expression));
        StringValue fileName = (StringValue) value;
        IDict<String, BufferedReader> fileTable = state.getFileTable();
        if (!fileTable.containsKey(fileName.getVal()))
            throw new InterpreterError(String.format("ERROR: %s is not present in the symTable", value));
        BufferedReader br = fileTable.get(fileName.getVal());
        try {
            br.close();
        } catch (IOException e) {
            throw new InterpreterError(String.format("ERROR: Unexpected error in closing %s", value));
        }
        fileTable.remove(fileName.getVal());
        return null;
    }

    @Override
    public String toString() {
        return String.format("CloseReadFile{%s}", expression);
    }

}
