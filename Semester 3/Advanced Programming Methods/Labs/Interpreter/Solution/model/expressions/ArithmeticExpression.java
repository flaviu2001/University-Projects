package model.expressions;

import exceptions.InterpreterError;
import model.adt.IDict;
import model.adt.IHeap;
import model.types.IntType;
import model.types.Type;
import model.values.Value;
import model.values.IntValue;

public class ArithmeticExpression extends BinaryExpression{
    public ArithmeticExpression(OPERATOR _operator, Expression _lhs, Expression _rhs) {
        super(_operator, _lhs, _rhs);
    }

    @Override
    public Type typeCheck(IDict<String, Type> typeTable) throws InterpreterError {
        Type type1, type2;
        type1 = lhs.typeCheck(typeTable);
        type2 = rhs.typeCheck(typeTable);
        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType()))
                return new IntType();
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
        switch (operator) {
            case ADD:
                return new IntValue(lhsValue.getVal() + rhsValue.getVal());
            case SUBSTR:
                return new IntValue(lhsValue.getVal() - rhsValue.getVal());
            case MULT:
                return new IntValue(lhsValue.getVal() * rhsValue.getVal());
            case DIV:
                if (rhsValue.getVal() == 0)
                    throw new InterpreterError("ERROR: Division by 0 is forbidden");
                return new IntValue(lhsValue.getVal() / rhsValue.getVal());
            default:
                throw new InterpreterError(String.format("ERROR: Invalid operator %s between %s and %s",
                        operator,
                        lhs.toString(),
                        rhs.toString()));
        }
    }
}
