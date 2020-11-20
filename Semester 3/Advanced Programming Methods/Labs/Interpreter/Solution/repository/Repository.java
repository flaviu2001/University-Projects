package repository;

import model.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Repository implements IRepository {
    private List<ProgramState> programStates;
    private final String logFilePath;

    public Repository(String _logFilePath) {
        programStates = new LinkedList<>();
        logFilePath = _logFilePath;
    }

    @Override
    public void addPrg(ProgramState newPrg) {
        programStates.add(newPrg);
    }

    @Override
    public List<ProgramState> getProgramStates() {
        return programStates;
    }

    @Override
    public void setProgramStates(List<ProgramState> prgStates) {
        this.programStates = prgStates;
    }

    @Override
    public void logProgramStateExecution(ProgramState programState) throws IOException {
        PrintWriter logFile;
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        logFile.println(programState);
        logFile.close();
    }
}
