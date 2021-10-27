package model.expressions;
import exceptions.InterpreterError;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.Type;
import model.values.Value;

public interface Expression {
    Value eval(IDict<String, Value> symbolTable, IHeap heap) throws InterpreterError;
    Type typeCheck(IDict<String, Type> typeTable) throws InterpreterError;
}
