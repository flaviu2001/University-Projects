package controller;

import exceptions.InterpreterError;
import model.ProgramState;
import model.statements.Statement;
import repository.IRepository;

public class Controller {
    private final IRepository repository;
    private boolean displayBit;

    public Controller(IRepository iRepository) {
        repository = iRepository;
        displayBit = false;
    }

    public void addProgram(ProgramState newPrg) {
        repository.addPrg(newPrg);
    }

    public void setDisplayBit(boolean newState) {
        displayBit = newState;
    }

    public ProgramState oneStep(ProgramState state) throws InterpreterError {
        Statement top = state.getExeStack().pop();
        top.execute(state);
        return state;
    }

    public void allSteps() throws InterpreterError {
        ProgramState state = repository.getCrtPrg();
        if (displayBit)
            System.out.println(state);
        repository.logProgramStateExecution(state);
        while (!state.getExeStack().isEmpty()) {
            oneStep(state);
            if (displayBit)
                System.out.println(state);
            repository.logProgramStateExecution(state);
        }
        System.out.println(state.outToString());
    }
}
