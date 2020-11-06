package model.adt;

public interface IList<T> extends Iterable<T>{
    void add(T v);
    T pop();
    boolean isEmpty();

}
