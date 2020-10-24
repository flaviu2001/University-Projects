package model.exp;
import exceptions.InterpreterError;
import model.adt.IDict;
import model.values.IValue;

public class VariableExpression implements IExpression{
    String key;

    public VariableExpression(String _key) {
        key = _key;
    }

    public IValue eval(IDict<String,IValue> symTable) throws InterpreterError {
        return symTable.lookup(key);
    }

    public String toString() {
        return String.format("VariableExpression{%s}", key);
    }
}
