package model.expressions;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.Type;
import model.values.Value;

public class VariableExpression implements Expression {
    private final String key;

    public VariableExpression(String _key) {
        key = _key;
    }

    @Override
    public Type typeCheck(IDict<String, Type> typeTable) {
        return typeTable.get(key);
    }

    @Override
    public Value eval(IDict<String, Value> symbolTable, IHeap heap) {
        return symbolTable.get(key);
    }

    @Override
    public String toString() {
        return String.format("VariableExpression{%s}", key);
    }
}
