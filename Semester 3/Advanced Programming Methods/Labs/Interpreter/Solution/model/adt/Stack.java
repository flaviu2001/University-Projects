package model.adt;
import exceptions.InterpreterError;

import java.util.Deque;
import java.util.LinkedList;

public class Stack<T> implements IStack<T> {
    Deque<T> deque;

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
    public void push(T v) {
        deque.push(v);
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (T elem : deque) {
            stringBuilder.append("[").append(elem.toString()).append(";]\n");
        }
        return stringBuilder.toString();
    }
}
