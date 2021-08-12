package model.adt;

import exceptions.InterpreterError;

import java.util.function.Consumer;

public interface IList<T> extends Iterable<T>{
    void add(T v);
    T pop() throws InterpreterError;
    boolean isEmpty();
    @Override
    void forEach(Consumer<? super T> action);
    java.util.List<T> getList();
}
