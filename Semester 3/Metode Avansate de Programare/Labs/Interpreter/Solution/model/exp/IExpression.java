package model.exp;
import exceptions.InterpreterError;
import model.adt.IDict;
import model.values.IValue;

public interface IExpression {
    public IValue eval(IDict<String,IValue> symTable) throws InterpreterError;
}
