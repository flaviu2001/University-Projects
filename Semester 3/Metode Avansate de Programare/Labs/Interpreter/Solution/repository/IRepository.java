package repository;
import exceptions.InterpreterError;
import model.PrgState;

public interface IRepository {
    void addPrg(PrgState newPrg);
    PrgState getCrtPrg() throws InterpreterError;
}
