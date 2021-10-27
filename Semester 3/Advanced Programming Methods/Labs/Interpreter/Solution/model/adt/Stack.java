package model.adt;
import exceptions.InterpreterError;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class Stack<T> implements IStack<T> {
    private final Deque<T> deque;

    public Stack(){
        deque = new LinkedList<>();
    }

    @Override
    public T pop() throws InterpreterError {
        if (deque.isEmpty())
            throw new InterpreterError("STACK ERROR: Stack is empty");
        return deque.pop();
    }

    @Override
    public T peek() throws InterpreterError {
        if (deque.isEmpty())
            throw new InterpreterError("STACK ERROR: Stack is empty");
        return deque.peek();
    }

    @Override
    public void push(T v) {
        deque.push(v);
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    @Override
    public Deque<T> getStack() {
        return deque;
    }
}
