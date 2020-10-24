package repository;
import exceptions.InterpreterError;
import model.PrgState;
import model.adt.List;

public class Repository implements IRepository {
    List<PrgState> myPrgStates;

    public Repository() {
        myPrgStates = new List<>();
    }

    @Override
    public PrgState getCrtPrg() throws InterpreterError {
        if (myPrgStates.isEmpty())
            throw new InterpreterError("REPO ERROR: list of program states is empty");
        return myPrgStates.pop();
    }

    @Override
    public void addPrg(PrgState newPrg) {
        myPrgStates.add(newPrg);
    }
}
