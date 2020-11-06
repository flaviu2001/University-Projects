package repository;
import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Repository implements IRepository {
    private final List<ProgramState> myPrgStates;
    private final String logFilePath;

    public Repository(String _logFilePath) {
        myPrgStates = new List<>();
        logFilePath = _logFilePath;
    }

    @Override
    public ProgramState getCrtPrg() throws InterpreterError {
        if (myPrgStates.isEmpty())
            throw new InterpreterError("REPO ERROR: list of program states is empty");
        return myPrgStates.pop();
    }

    @Override
    public void addPrg(ProgramState newPrg) {
        myPrgStates.add(newPrg);
    }

    @Override
    public void logProgramStateExecution(ProgramState programState) throws InterpreterError {
        PrintWriter logFile;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        } catch (IOException e) {
            throw new InterpreterError("ERROR: Failed to log the program state execution");
        }
        logFile.println(programState);
        logFile.close();
    }
}
