package model.expressions;

import exceptions.InterpreterError;
import model.adt.IDict;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Value;

public class RelationalExpression extends BinaryExpression{
    public RelationalExpression(OPERATOR _operator, Expression _lhs, Expression _rhs) {
        super(_operator, _lhs, _rhs);
    }

    private IntValue getValue(Expression expression, IDict<String, Value> symTable, IDict<Integer, Value> heap) throws InterpreterError {
        Value value = expression.eval(symTable, heap);
        if (value instanceof IntValue)
            return (IntValue) value;
        throw new InterpreterError(String.format("ERROR: %s is not of type IntType", value.toString()));
    }

    @Override
    public Value eval(IDict<String, Value> symTable, IDict<Integer, Value> heap) throws InterpreterError {
        IntValue lhsValue = getValue(lhs, symTable, heap);
        IntValue rhsValue = getValue(rhs, symTable, heap);
        return switch (operator) {
            case MORE -> new BoolValue(lhsValue.getVal() > rhsValue.getVal());
            case MORE_EQUAL -> new BoolValue(lhsValue.getVal() >= rhsValue.getVal());
            case LESS -> new BoolValue(lhsValue.getVal() < rhsValue.getVal());
            case LESS_EQUAL -> new BoolValue(lhsValue.getVal() <= rhsValue.getVal());
            case EQUAL -> new BoolValue(lhsValue.getVal() == rhsValue.getVal());
            case NOT_EQUAL -> new BoolValue(lhsValue.getVal() != rhsValue.getVal());
            default -> throw new InterpreterError(String.format("ERROR: Invalid operator %s between %s and %s",
                    operator,
                    lhs.toString(),
                    rhs.toString()));
        };
    }
}
