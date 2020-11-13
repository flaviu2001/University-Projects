package controller;

import exceptions.InterpreterError;
import model.ProgramState;
import model.statements.Statement;
import model.values.ReferenceValue;
import model.values.Value;
import repository.IRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repository;

    public Controller(IRepository iRepository) {
        repository = iRepository;
    }

    public void addProgram(ProgramState newPrg) {
        repository.addPrg(newPrg);
    }

    Map<Integer, Value> garbageCollector(Set<Integer> symTableAddr, Map<Integer, Value> heap){
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Set<Integer> getAddrFromSymTable(Collection<Value> symTableValues, Map<Integer, Value> heap){
        Set<Integer> toReturn = new TreeSet<>();
        symTableValues.stream()
                .filter(v -> v instanceof ReferenceValue)
                .forEach(v -> {
                    while (v instanceof ReferenceValue) {
                        toReturn.add(((ReferenceValue)v).getAddress());
                        v = heap.get(((ReferenceValue)v).getAddress());
                    }
                });
        return toReturn;
    }

    public ProgramState oneStep(ProgramState state) throws InterpreterError {
        Statement top = state.getExeStack().pop();
        top.execute(state);
        return state;
    }

    public void allSteps() throws InterpreterError {
        ProgramState state = repository.getCrtPrg();
        repository.logProgramStateExecution(state, true);
        while (!state.getExeStack().isEmpty()) {
            oneStep(state);
            repository.logProgramStateExecution(state, true);
            state.getHeap().setContent(
                    garbageCollector(
                            getAddrFromSymTable(
                                    state.getSymTable().getContent().values(),
                                    state.getHeap().getContent()
                            ),
                            state.getHeap().getContent()
                    )
            );
            repository.logProgramStateExecution(state, false);
        }
        System.out.println(state.outToString());
    }
}
