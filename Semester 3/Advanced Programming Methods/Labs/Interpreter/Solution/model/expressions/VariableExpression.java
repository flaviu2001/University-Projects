package model.expressions;
import model.adt.IDict;
import model.values.Value;

public class VariableExpression implements Expression {
    private final String key;

    public VariableExpression(String _key) {
        key = _key;
    }

    public Value eval(IDict<String, Value> symTable) {
        return symTable.get(key);
    }

    public String toString() {
        return String.format("VariableExpression{%s}", key);
    }
}
