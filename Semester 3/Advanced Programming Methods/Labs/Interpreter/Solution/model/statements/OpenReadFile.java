package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.StringType;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenReadFile implements Statement{
    private final Expression expression;

    public OpenReadFile(Expression _expression) {
        expression = _expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        Value value = expression.eval(state.getSymTable());
        if (!value.getType().equals(new StringType()))
            throw new InterpreterError(String.format("ERROR: %s does not evaluate to StringValue", expression));
        StringValue fileName = (StringValue) value;
        IDict<String, BufferedReader> fileTable = state.getFileTable();
        if (fileTable.containsKey(fileName.getVal()))
            throw new InterpreterError(String.format("ERROR: %s is already opened", fileName));
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName.getVal()));
        }catch (FileNotFoundException e) {
            throw new InterpreterError(String.format("ERROR: %s could not be opened", fileName));
        }
        fileTable.put(fileName.getVal(), br);
        return state;
    }

    @Override
    public String toString() {
        return String.format("OpenReadFile{%s}", expression);
    }
}
