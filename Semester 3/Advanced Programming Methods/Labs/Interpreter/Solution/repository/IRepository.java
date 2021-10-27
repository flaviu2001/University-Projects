package repository;
import model.ProgramState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    void addPrg(ProgramState newPrg);
    void logProgramStateExecution(ProgramState programState) throws IOException;
    List<ProgramState> getProgramStates();
    void setProgramStates(List<ProgramState> prgStates);
}
