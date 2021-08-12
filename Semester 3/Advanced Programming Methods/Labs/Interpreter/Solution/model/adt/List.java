package model.adt;

import exceptions.InterpreterError;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class List<T> implements IList<T> {
    private final java.util.List<T> list;

    public List() {
        list = new LinkedList<>();
    }

    public List(java.util.List<T> _list) {
        list = _list;
    }
    @Override
    public void add(T v) {
        synchronized (list) {
            list.add(v);
        }
    }

    @Override
    public T pop() throws InterpreterError {
        synchronized (list) {
            if (list.size() > 0) {
                T toReturn = list.get(0);
                list.remove(0);
                return toReturn;
            }
        }
        throw new InterpreterError("ERROR: List empty when popping");
    }

    @Override
    public boolean isEmpty() {
        synchronized (list) {
            return list.isEmpty();
        }
    }

    @Override
    public Iterator<T> iterator() {
        synchronized (list) {
            return list.iterator();
        }
    }

    @Override
    public void forEach(Consumer<? super T> action){
        synchronized (list) {
            list.forEach(action);
        }
    }
    @Override
    public java.util.List<T> getList() {
        return list;
    }
}
