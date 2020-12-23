package model.expressions;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.Type;
import model.values.Value;

public class ValueExpression implements Expression {
    private final Value value;

    public ValueExpression(Value _value) {
        value = _value;
    }

    @Override
    public Type typeCheck(IDict<String, Type> typeTable) {
        return value.getType();
    }

    @Override
    public Value eval(IDict<String, Value> symbolTable, IHeap heap) {
        return value;
    }

    @Override
    public String toString() {
        return String.format("ValueExpression{%s}", value.toString());
    }
}
