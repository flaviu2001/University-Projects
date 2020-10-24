package model.adt;

import exceptions.InterpreterError;

public interface IDict<T1,T2>{
    void add(T1 v1, T2 v2) throws InterpreterError;
    void update(T1 v1, T2 v2) throws InterpreterError;
    T2 lookup(T1 id) throws InterpreterError;
    boolean isDefined(T1 id);
}
