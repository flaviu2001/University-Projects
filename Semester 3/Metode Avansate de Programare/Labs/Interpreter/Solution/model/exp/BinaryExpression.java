package model.exp;
import exceptions.InterpreterError;
import model.adt.IDict;
import model.types.BoolType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;

public class BinaryExpression implements IExpression {
    String operator;
    IExpression lhs, rhs;

    public BinaryExpression(String _operator, IExpression _lhs, IExpression _rhs) {
        operator = _operator;
        lhs = _lhs;
        rhs = _rhs;
    }

    public IValue eval(IDict<String, IValue> symTable) throws InterpreterError {
        IValue lhs_value = lhs.eval(symTable);
        IValue rhs_value = rhs.eval(symTable);
        if (lhs_value.getType().equals(new IntType()) && rhs_value.getType().equals(new IntType())) {
            IntValue cast_lhs = (IntValue) lhs_value;
            IntValue cast_rhs = (IntValue) rhs_value;
            switch (operator) {
                case "+":
                    return new IntValue(cast_lhs.getVal() + cast_rhs.getVal());
                case "-":
                    return new IntValue(cast_lhs.getVal() - cast_rhs.getVal());
                case "*":
                    return new IntValue(cast_lhs.getVal() * cast_rhs.getVal());
                case "/":
                    if (cast_rhs.getVal() == 0)
                        throw new InterpreterError("ERROR: Division by 0 is forbidden");
                    return new IntValue(cast_lhs.getVal() / cast_rhs.getVal());
                case "==":
                    return new BoolValue(cast_lhs.getVal() == cast_rhs.getVal());
                default:
                    throw new InterpreterError(String.format("ERROR: Undefined operator %s between int and int", operator));
            }
        }else if (lhs_value.getType().equals(new BoolType()) && rhs_value.getType().equals(new BoolType())) {
            BoolValue b1 = (BoolValue) lhs_value;
            BoolValue b2 = (BoolValue) rhs_value;
            return switch (operator) {
                case "&&" -> new BoolValue(b1.getVal() && b2.getVal());
                case "||" -> new BoolValue(b1.getVal() || b2.getVal());
                case "==" -> new BoolValue(b1.getVal() == b2.getVal());
                default -> throw new InterpreterError(String.format("ERROR: Undefined operator %s between bool and bool", operator));
            };
        }
        throw new InterpreterError(String.format("ERROR: Incompatible types %s and %s",
                lhs_value.getType().toString(),
                rhs_value.getType().toString()));
    }

    public String toString() {
        return String.format("BinaryExpression{%s %s %s}", lhs.toString(), operator, rhs.toString());
    }
}
