package model.adt;

import exceptions.InterpreterError;

public interface IStack<T> extends Iterable<T>{
    T pop() throws InterpreterError;
    void push(T v);
    boolean isEmpty();
}

