package view.cli;

import controller.Controller;
import model.ProgramState;
import model.statements.Statement;
import repository.IRepository;
import repository.Repository;
import view.cli.command.ExitCommand;
import view.cli.command.RunExample;

import static view.Examples.exampleList;

public class View {

    public static void main(String[] args) {
        TextMenu menu = new TextMenu();
        int pos = 1;
        menu.addCommand(new ExitCommand("0", "exit"));
        for (Statement statement : exampleList()) {
            ProgramState programState = new ProgramState(statement);
            IRepository repository = new Repository("log.txt");
            repository.addPrg(programState);
            Controller controller = new Controller(repository);
            menu.addCommand(new RunExample(Integer.toString(pos), statement.toString(), controller));
            ++pos;
        }
        menu.show();
    }
}
