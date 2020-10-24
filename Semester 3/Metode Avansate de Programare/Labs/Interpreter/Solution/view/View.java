package view;

import controller.Controller;
import exceptions.InterpreterError;
import model.PrgState;
import model.exp.BinaryExpression;
import model.exp.ValueExpression;
import model.exp.VariableExpression;
import model.statement.*;
import model.types.BoolType;
import model.types.IntType;
import model.values.BoolValue;
import model.values.IntValue;
import repository.Repository;

import java.util.Scanner;

public class View {

    static Repository myRepository = new Repository();
    static Controller myController = new Controller(myRepository);

    static IStatement buildExample(IStatement... statements) {
        if (statements.length == 0)
            return new NopStatement();
        if (statements.length == 1)
            return statements[0];
        IStatement finalStatement = new CompoundStatement(statements[0], statements[1]);
        for (int i = 2; i < statements.length; ++i)
            finalStatement = new CompoundStatement(finalStatement, statements[i]);
        return finalStatement;
    }

    static IStatement[] exampleList() {
        IStatement example0 = buildExample(
                new DeclarationStatement("v",new IntType()),
                new AssignmentStatement("v",
                        new ValueExpression(new IntValue(2))
                ),
                new PrintStatement(new VariableExpression("v"))
        );
        IStatement example1 = buildExample(
                new DeclarationStatement("a",new IntType()),
                new DeclarationStatement("b",new IntType()),
                new AssignmentStatement("a",
                        new BinaryExpression("+",
                                new ValueExpression(new IntValue(2)),
                                new BinaryExpression("*",
                                        new ValueExpression(new IntValue(3)),
                                        new ValueExpression(new IntValue(5))
                                )
                        )
                ),
                new AssignmentStatement("b",
                        new BinaryExpression("+",
                                new VariableExpression("a"),
                                new ValueExpression(new IntValue(1))
                        )
                ),
                new PrintStatement(new VariableExpression("b"))
        );
        IStatement example2 = buildExample(
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
        return new IStatement[]{example0, example1, example2};
    }

    public static void main(String[] args) {
        System.out.print("Type the number of the example you wish to run (between 0 and 2): ");
        Scanner user = new Scanner(System.in);
        PrgState myPrgState = new PrgState(exampleList()[user.nextInt()]);
        myController.addProgram(myPrgState);
        try {
            myController.allStep();
            System.out.println(myPrgState.getOut());
        } catch (InterpreterError interpreterError) {
            System.out.println(interpreterError.getMessage());
        }
    }
}
