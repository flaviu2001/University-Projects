package model.exp;
import model.adt.IDict;
import model.values.IValue;

public class ValueExpression implements IExpression{
    IValue value;

    public ValueExpression(IValue _value) {
        value = _value;
    }

    public IValue eval(IDict<String,IValue> symTable) {
        return value;
    }

    public String toString() {
        return String.format("ValueExpression{%s}", value.toString());
    }
}
