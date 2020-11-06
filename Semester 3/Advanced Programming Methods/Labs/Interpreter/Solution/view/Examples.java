package view;

import model.expressions.ArithmeticExpression;
import model.expressions.BinaryExpression;
import model.expressions.ValueExpression;
import model.expressions.VariableExpression;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.StringType;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;

public class Examples {
    public static Statement buildExample(Statement... statements) {
        if (statements.length == 0)
            return new NopStatement();
        if (statements.length == 1)
            return statements[0];
        Statement finalStatement = new CompoundStatement(statements[0], statements[1]);
        for (int i = 2; i < statements.length; ++i)
            finalStatement = new CompoundStatement(finalStatement, statements[i]);
        return finalStatement;
    }

    public static Statement[] exampleList() {
        Statement example0 = buildExample(
                new DeclarationStatement("v",new IntType()),
                new AssignmentStatement("v",
                        new ValueExpression(new IntValue(2))
                ),
                new PrintStatement(new VariableExpression("v"))
        );
        Statement example1 = buildExample(
                new DeclarationStatement("a",new IntType()),
                new DeclarationStatement("b",new IntType()),
                new AssignmentStatement("a",
                        new ArithmeticExpression(BinaryExpression.OPERATOR.ADD,
                                new ValueExpression(new IntValue(2)),
                                new ArithmeticExpression(BinaryExpression.OPERATOR.MULT,
                                        new ValueExpression(new IntValue(3)),
                                        new ValueExpression(new IntValue(5))
                                )
                        )
                ),
                new AssignmentStatement("b",
                        new ArithmeticExpression(BinaryExpression.OPERATOR.ADD,
                                new VariableExpression("a"),
                                new ValueExpression(new IntValue(1))
                        )
                ),
                new PrintStatement(new VariableExpression("b"))
        );
        Statement example2 = buildExample(
                new DeclarationStatement("a",new BoolType()),
                new DeclarationStatement("v", new IntType()),
                new AssignmentStatement("a",
                        new ValueExpression(new BoolValue(true))
                ),
                new IfStatement(
                        new VariableExpression("a"),
                        new AssignmentStatement("v",
                                new ValueExpression(new IntValue(2))
                        ),
                        new AssignmentStatement("v",
                                new ValueExpression(new IntValue(3))
                        )
                ),
                new PrintStatement(new VariableExpression("v"))
        );
        Statement example3 = buildExample(
                new DeclarationStatement("a", new BoolType()),
                new AssignmentStatement("a", new ValueExpression(new IntValue(2)))
        );
        Statement example4 = buildExample(
                new DeclarationStatement("file", new StringType()),
                new AssignmentStatement("file", new ValueExpression(new StringValue("test.in"))),
                new OpenReadFile(new VariableExpression("file")),
                new DeclarationStatement("x", new IntType()),
                new ReadFile(new VariableExpression("file"), "x"),
                new PrintStatement(new VariableExpression("x")),
                new ReadFile(new VariableExpression("file"), "x"),
                new PrintStatement(new VariableExpression("x")),
                new CloseReadFile(new VariableExpression("file"))
        );
        return new Statement[]{example0, example1, example2, example3, example4};
    }
}
