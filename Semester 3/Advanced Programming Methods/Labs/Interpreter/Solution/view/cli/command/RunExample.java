package view.cli.command;

import controller.Controller;
import exceptions.InterpreterError;
import model.adt.IList;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String desc, Controller _controller){
        super(key, desc);
        controller = _controller;
    }

    @Override
    public void execute() {
        try{
            IList<String> out = controller.allSteps();
            out.forEach(System.out::println);
        }
        catch (InterpreterError error) {
            System.out.println(error.getMessage());
        }
    }
}
