package model.expressions;

public abstract class BinaryExpression implements Expression {
    protected final OPERATOR operator;
    protected final Expression lhs, rhs;

    public enum OPERATOR {
        ADD,
        SUBSTR,
        MULT,
        DIV,
        AND,
        OR,
        LESS,
        LESS_EQUAL,
        MORE,
        MORE_EQUAL,
        EQUAL,
        NOT_EQUAL
    }

    public BinaryExpression(OPERATOR _operator, Expression _lhs, Expression _rhs) {
        operator = _operator;
        lhs = _lhs;
        rhs = _rhs;
    }

    public String toString() {
        return String.format("BinaryExpression{%s %s %s}", lhs.toString(), operator, rhs.toString());
    }
}
