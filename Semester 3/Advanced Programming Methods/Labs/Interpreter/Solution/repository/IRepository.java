package repository;
import exceptions.InterpreterError;
import model.ProgramState;

public interface IRepository {
    void addPrg(ProgramState newPrg);
    ProgramState getCrtPrg() throws InterpreterError;
    void logProgramStateExecution(ProgramState programState, boolean beforeGarbageCollector) throws InterpreterError;
}
