package model.expressions;

import exceptions.InterpreterError;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Value;

public class RelationalExpression extends BinaryExpression{
    public RelationalExpression(OPERATOR _operator, Expression _lhs, Expression _rhs) {
        super(_operator, _lhs, _rhs);
    }

    @Override
    public Type typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        Type type1, type2;
        type1 = lhs.typeCheck(typeTable);
        type2 = rhs.typeCheck(typeTable);
        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType()))
                return new BoolType();
            else
                throw new InterpreterError("ERROR: The second operand is not an integer");
        }else
            throw new InterpreterError("ERROR: The first operand is not an integer");
    }

    private IntValue getValue(Expression expression, IDict<String, Value> symTable, IHeap heap) throws InterpreterError {
        Value value = expression.eval(symTable, heap);
        if (value instanceof IntValue)
            return (IntValue) value;
        throw new InterpreterError(String.format("ERROR: %s is not of type IntType", value.toString()));
    }

    @Override
    public Value eval(IDict<String, Value> symbolTable, IHeap heap) throws InterpreterError {
        IntValue lhsValue = getValue(lhs, symbolTable, heap);
        IntValue rhsValue = getValue(rhs, symbolTable, heap);
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
