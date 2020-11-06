package view.command;

import controller.Controller;
import exceptions.InterpreterError;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String desc, Controller _controller){
        super(key, desc);
        controller = _controller;
    }

    @Override
    public void execute() {
        try{
            controller.allSteps();
        }
        catch (InterpreterError error) {
            System.out.println(error.getMessage());
        }
    }
}