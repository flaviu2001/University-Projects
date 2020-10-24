package model;
import model.adt.*;
import model.statement.IStatement;
import model.values.IValue;

public class PrgState {
    private final IStack<IStatement> exeStack;
    private final IDict<String, IValue> symTable;
    private final IList<String> out;

    public IStack<IStatement> getExeStack() {
        return exeStack;
    }

    public IDict<String, IValue> getSymTable() {
        return symTable;
    }

    public IList<String> getOut() {
        return out;
    }

    public PrgState(IStatement originalProgram) {
        exeStack = new Stack<IStatement>();
        symTable = new Dict<String, IValue>();
        out = new List<String>();
        exeStack.push(originalProgram);
    }

    public PrgState(IStack<IStatement> _exeStack, IDict<String, IValue> _symTable, IList<String> _out) {
        exeStack = _exeStack;
        symTable = _symTable;
        out = _out;
    }

    @Override
    public String toString() {
        return String.format("EXE_STACK:\n%s\nSYM_TABLE:\n%s\nOUT:\n%s", exeStack.toString(), symTable.toString(), out.toString());
    }
}