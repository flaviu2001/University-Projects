package model.expressions;

import exceptions.InterpreterError;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.BoolType;
import model.types.Type;
import model.values.BoolValue;
import model.values.Value;

public class LogicExpression extends BinaryExpression {
    public LogicExpression(OPERATOR _operator, Expression _lhs, Expression _rhs) {
        super(_operator, _lhs, _rhs);
    }

    @Override
    public Type typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        Type type1, type2;
        type1 = lhs.typeCheck(typeTable);
        type2 = rhs.typeCheck(typeTable);
        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType()))
                return new BoolType();
            else
                throw new InterpreterError("ERROR: The second operand is not a bool");
        }else
            throw new InterpreterError("ERROR: The first operand is not a bool");
    }

    private BoolValue getValue(Expression expression, IDict<String, Value> symTable, IHeap heap) throws InterpreterError {
        Value value = expression.eval(symTable, heap);
        if (value instanceof BoolValue)
            return (BoolValue) value;
        throw new InterpreterError(String.format("ERROR: %s is not of type BoolType", value.toString()));
    }

    @Override
    public Value eval(IDict<String, Value> symbolTable, IHeap heap) throws InterpreterError {
        BoolValue lhsValue = getValue(lhs, symbolTable, heap);
        BoolValue rhsValue = getValue(rhs, symbolTable, heap);
        return switch (operator) {
            case AND -> new BoolValue(lhsValue.getVal() && rhsValue.getVal());
            case OR -> new BoolValue(lhsValue.getVal() || rhsValue.getVal());
            default -> throw new InterpreterError(String.format("ERROR: Invalid operator %s between %s and %s",
                    operator,
                    lhs.toString(),
                    rhs.toString()));
        };
    }
}
