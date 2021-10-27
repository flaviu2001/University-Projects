package model;

import exceptions.InterpreterError;
import model.adt.*;
import model.statements.Statement;
import model.values.Value;

import java.io.BufferedReader;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class ProgramState {
    private final IStack<Statement> executionStack;
    private final IDict<String, Value> symTable;
    private final IList<String> out;
    private final IDict<String, BufferedReader> fileTable;
    private final IHeap heap;
    private static final TreeSet<Integer> ids = new TreeSet<>();
    public final Integer id;

    public ProgramState(Statement originalProgram) {
        executionStack = new Stack<>();
        symTable = new Dict<>();
        out = new List<>();
        fileTable = new Dict<>();
        heap = new Heap();
        executionStack.push(originalProgram);
        id = newId();
    }

    public ProgramState(IStack<Statement> _exeStack,
                        IDict<String, Value> _symTable,
                        IList<String> _out,
                        IDict<String, BufferedReader> _fileTable,
                        IHeap _heap) {
        executionStack = _exeStack;
        symTable = _symTable;
        out = _out;
        fileTable = _fileTable;
        heap = _heap;
        id = newId();
    }

    public IStack<Statement> getExecutionStack() {
        return executionStack;
    }

    public IDict<String, Value> getSymTable() {
        return symTable;
    }

    public IList<String> getOut() {
        return out;
    }

    public IHeap getHeap() {
        return heap;
    }

    private static Integer newId() {
        Random random = new Random();
        Integer id;
        synchronized (ids) {
            do {
                id = random.nextInt();
            } while (ids.contains(id));
            ids.add(id);
        }
        return id;
    }

    public boolean isCompleted() {
        return executionStack.isEmpty();
    }

    public ProgramState oneStep() throws InterpreterError {
        if (executionStack.isEmpty())
            throw new InterpreterError("ERROR: Execution stack is empty when attempting to execute one step");
        Statement top = executionStack.pop();
        return top.execute(this);
    }

    public String exeStackToString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        for (Statement elem : executionStack) {
            exeStackStringBuilder.append("[\n").append(elem.toString()).append("\n]\n");
        }
        return exeStackStringBuilder.toString();
    }

    public String symTableToString() {
        StringBuilder symTableStringBuilder = new StringBuilder();
        for (String key : symTable.keySet())
            symTableStringBuilder.append(String.format("%s:%s\n", key, symTable.get(key).toString()));
        return symTableStringBuilder.toString();
    }

    public String outToString() {
        StringBuilder outStringBuilder = new StringBuilder();
        for (String obj : out)
            outStringBuilder.append(obj).append("\n");
        return outStringBuilder.toString();
    }

    public String heapToString() {
        StringBuilder outStringBuilder = new StringBuilder();
        Map<Integer, Value> map = heap.getContent();
        for (Integer key : map.keySet())
            outStringBuilder.append(key).append(" -> ").append(map.get(key)).append("\n");
        return outStringBuilder.toString();
    }

    public IDict<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public String fileTableToString() {
        StringBuilder fileTableStringBuilder = new StringBuilder();
        for (String obj : fileTable.keySet())
            fileTableStringBuilder.append(obj).append("\n");
        return fileTableStringBuilder.toString();
    }

    @Override
    public String toString() {
        return String.format("------ID------\n%d\n------EXE_STACK------\n%s------SYM_TABLE------\n%s------OUT------\n%s------FILE_TABLE------\n%s------HEAP------\n%s",
                id,
                exeStackToString(),
                symTableToString(),
                outToString(),
                fileTableToString(),
                heapToString());
    }
}