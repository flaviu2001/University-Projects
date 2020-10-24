package model.adt;

import exceptions.InterpreterError;

public interface IStack<T> {
    T pop() throws InterpreterError;
    void push(T v);
    boolean isEmpty();
}

