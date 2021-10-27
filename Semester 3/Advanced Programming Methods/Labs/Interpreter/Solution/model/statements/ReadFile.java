package model.statements;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IDict;
import model.expressions.Expression;
import model.types.IntType;
import model.types.StringType;
import model.types.Type;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements Statement {
    private final Expression expression;
    private final String varName;

    public ReadFile(Expression _expression, String _varName) {
        expression = _expression;
        varName = _varName;
    }

    @Override
    public IDict<String, Type> typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        if (!expression.typeCheck(typeTable).equals(new StringType()))
            throw new InterpreterError("ERROR: ReadFile requires a string as expression parameter");
        if (!typeTable.get(varName).equals(new IntType()))
            throw new InterpreterError("ERROR: ReadFile requires an int as variable parameter");
        return typeTable;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterError {
        IDict<String, Value> symTable = state.getSymTable();
        IDict<String, BufferedReader> fileTable = state.getFileTable();
        if (!symTable.containsKey(varName))
            throw new InterpreterError(String.format("ERROR: %s is not present in the symTable", varName));
        Value value = symTable.get(varName);
        if (!value.getType().equals(new IntType()))
            throw new InterpreterError(String.format("ERROR: %s is not of type IntType", value));
        value = expression.eval(symTable, state.getHeap());
        if (!value.getType().equals(new StringType()))
            throw new InterpreterError(String.format("ERROR: %s does not evaluate to StringType", value));
        StringValue castValue = (StringValue) value;
        if (!fileTable.containsKey(castValue.getVal()))
            throw new InterpreterError(String.format("ERROR: the fileTable does not contain %s", castValue));
        BufferedReader br = fileTable.get(castValue.getVal());
        try {
            String line = br.readLine();
            if (line == null)
                line = "0";
            symTable.put(varName, new IntValue(Integer.parseInt(line)));
        } catch (IOException e) {
            throw new InterpreterError(String.format("ERROR: could not read from file %s", castValue));
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("ReadFile{%s, %s}", expression, varName);
    }
}
