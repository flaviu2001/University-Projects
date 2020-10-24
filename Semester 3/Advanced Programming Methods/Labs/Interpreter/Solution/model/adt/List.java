package model.adt;

import java.util.LinkedList;
import java.util.Queue;

public class List<T> implements IList<T> {
    Queue<T> list;

    public List() {
        list = new LinkedList<>();
    }

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public T pop() {
        return list.poll();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (T obj : list) {
            stringBuilder.append(obj.toString());
        }
        return stringBuilder.toString();
    }
}
