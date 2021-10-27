package model.adt;

import exceptions.InterpreterError;

import java.util.Deque;

public interface IStack<T> extends Iterable<T>{
    T pop() throws InterpreterError;
    T peek() throws InterpreterError;
    void push(T v);
    boolean isEmpty();
    Deque<T> getStack();
}

