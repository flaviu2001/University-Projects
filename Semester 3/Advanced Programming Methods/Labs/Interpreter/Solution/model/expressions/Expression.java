package model.expressions;
import exceptions.InterpreterError;
import model.adt.IDict;
import model.adt.IHeap;
import model.values.Value;

public interface Expression {
    Value eval(IDict<String, Value> symTable, IHeap heap) throws InterpreterError;
}
