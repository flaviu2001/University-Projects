package model.expressions;
import model.adt.IDict;
import model.values.Value;

public class ValueExpression implements Expression {
    private final Value value;

    public ValueExpression(Value _value) {
        value = _value;
    }

    public Value eval(IDict<String, Value> symTable) {
        return value;
    }

    public String toString() {
        return String.format("ValueExpression{%s}", value.toString());
    }
}
