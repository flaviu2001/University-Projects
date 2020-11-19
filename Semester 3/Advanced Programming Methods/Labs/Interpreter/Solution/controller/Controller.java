package controller;

import exceptions.InterpreterError;
import model.ProgramState;
import model.adt.IList;
import model.values.ReferenceValue;
import model.values.Value;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

class Pair {
    ProgramState first;
    InterpreterError second;

    Pair(ProgramState _first, InterpreterError _second) {
        first = _first;
        second = _second;
    }
}

public class Controller {
    private final IRepository repository;
    private ExecutorService executor;

    public Controller(IRepository iRepository) {
        repository = iRepository;
    }

    public void addProgram(ProgramState newPrg) {
        repository.addPrg(newPrg);
    }

    List<ProgramState> removeCompletedPrograms(List<ProgramState> programStateList) {
        return programStateList.stream()
                .filter(p -> !p.isCompleted())
                .collect(Collectors.toList());
    }

    Map<Integer, Value> garbageCollector(Set<Integer> symTableAddr, Map<Integer, Value> heap){
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Set<Integer> getAddrFromSymTable(List<Collection<Value>> symTableValues, Map<Integer, Value> heap){
        Set<Integer> toReturn = new TreeSet<>();
        symTableValues.forEach(symTable -> symTable.stream()
                .filter(v -> v instanceof ReferenceValue)
                .forEach(v -> {
                    while (v instanceof ReferenceValue) {
                        toReturn.add(((ReferenceValue)v).getAddress());
                        v = heap.get(((ReferenceValue)v).getAddress());
                    }
                }));

        return toReturn;
    }

    public void oneStepForEachProgram(List<ProgramState> programStateList) throws InterpreterError {
        programStateList.forEach(prg -> {
            try {
                repository.logProgramStateExecution(prg);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        });
        List<Callable<ProgramState>> callList = programStateList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (p::oneStep))
                .collect(Collectors.toList());
        List<Pair> newProgramStateList = null;
        try {
            newProgramStateList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return new Pair(future.get(), null);
                        } catch (ExecutionException | InterruptedException e) {
                            if (e.getCause() instanceof InterpreterError)
                                return new Pair(null, (InterpreterError)e.getCause());
                            System.out.println(e.getMessage());
                            System.exit(1);
                            return null;
                        }
                    }).filter(pair -> pair.first != null || pair.second != null)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (Pair error : newProgramStateList)
            if (error.second != null)
                throw error.second;
        programStateList.addAll(newProgramStateList.stream().map(pair -> pair.first).collect(Collectors.toList()));
        repository.setProgramStates(programStateList);
    }

    public IList<String> allSteps() throws InterpreterError {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> programList = removeCompletedPrograms(repository.getProgramStates());
        IList<String> out = programList.get(0).getOut();
        while (!programList.isEmpty()) {
            ProgramState state = programList.get(0);
            state.getHeap().setContent(
                    garbageCollector(
                            getAddrFromSymTable(
                                    programList.stream().map(programState -> programState.getSymTable().getContent().values()).collect(Collectors.toList()),
                                    state.getHeap().getContent()
                            ),
                            state.getHeap().getContent()
                    )
            );
            oneStepForEachProgram(programList);
            programList = removeCompletedPrograms(repository.getProgramStates());
        }
        executor.shutdownNow();
        repository.setProgramStates(programList);
        return out;
    }
}
