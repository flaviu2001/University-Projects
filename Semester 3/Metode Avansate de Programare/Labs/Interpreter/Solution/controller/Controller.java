package controller;

import exceptions.InterpreterError;
import model.PrgState;
import model.statement.IStatement;
import repository.IRepository;

public class Controller {
    IRepository repository;

    public Controller(IRepository iRepository) {
        repository = iRepository;
    }

    public void addProgram(PrgState newPrg) {
        repository.addPrg(newPrg);
    }

    public PrgState oneStep(PrgState state) throws InterpreterError {
        IStatement top = state.getExeStack().pop();
        top.execute(state);
        return state;
    }

    public void allStep() throws InterpreterError {
        PrgState state = repository.getCrtPrg();
//        System.out.println(state);
        while (!state.getExeStack().isEmpty()) {
            oneStep(state);
//            System.out.println(state);
        }
    }
}
