package model.expressions;
import model.adt.IDict;
import model.values.Value;

public class VariableExpression implements Expression {
    private final String key;

    public VariableExpression(String _key) {
        key = _key;
    }

    @Override
    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) {
        return symTable.get(key);
    }

    @Override
    public String toString() {
        return String.format("VariableExpression{%s}", key);
    }
}
