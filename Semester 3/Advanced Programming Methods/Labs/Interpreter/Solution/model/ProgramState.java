package model;

import model.adt.*;
import model.statements.Statement;
import model.values.Value;

import java.io.BufferedReader;

public class ProgramState {
    private final IStack<Statement> exeStack;
    private final IDict<String, Value> symTable;
    private final IList<String> out;
    private final IDict<String, BufferedReader> fileTable;
    private final IDict<Integer, Value> heap;

    public ProgramState(Statement originalProgram) {
        exeStack = new Stack<>();
        symTable = new Dict<>();
        out = new List<>();
        fileTable = new Dict<>();
        heap = new Dict<>();
        exeStack.push(originalProgram);
    }

    public ProgramState(IStack<Statement> _exeStack,
                        IDict<String, Value> _symTable,
                        IList<String> _out,
                        IDict<String, BufferedReader> _fileTable,
                        IDict<Integer, Value> _heap) {
        exeStack = _exeStack;
        symTable = _symTable;
        out = _out;
        fileTable = _fileTable;
        heap = _heap;
    }

    public IStack<Statement> getExeStack() {
        return exeStack;
    }

    public IDict<String, Value> getSymTable() {
        return symTable;
    }

    public IList<String> getOut() {
        return out;
    }

    public IDict<Integer, Value> getHeap() {
        return heap;
    }

    public String exeStackToString() {
        StringBuilder exeStackStringBuilder = new StringBuilder();
        for (Statement elem : exeStack) {
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
        for (Integer key : heap.keySet())
            outStringBuilder.append(key).append(" -> ").append(heap.get(key)).append("\n");
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
        return String.format("------EXE_STACK------\n%s------SYM_TABLE------\n%s------OUT------\n%s------FILE_TABLE------\n%s------HEAP------\n%s",
                exeStackToString(),
                symTableToString(),
                outToString(),
                fileTableToString(),
                heapToString());
    }
}