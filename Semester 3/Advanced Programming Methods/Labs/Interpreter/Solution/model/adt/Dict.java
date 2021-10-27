package model.adt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Dict<T1,T2> implements IDict<T1,T2> {
    private final HashMap<T1, T2> dictionary;

    public Dict() {
        dictionary = new HashMap<>();
    }

    @Override
    public void put(T1 key, T2 value){
        synchronized (dictionary) {
            dictionary.put(key, value);
        }
    }

    @Override
    public T2 get(T1 key){
        synchronized (dictionary) {
            return dictionary.get(key);
        }
    }

    @Override
    public boolean containsKey(T1 id) {
        synchronized (dictionary) {
            return dictionary.containsKey(id);
        }
    }

    @Override
    public Set<T1> keySet() {
        synchronized (dictionary) {
            return dictionary.keySet();
        }
    }

    @Override
    public void remove(T1 key) {
        synchronized (dictionary) {
            dictionary.remove(key);
        }
    }

    @Override
    public Map<T1, T2> getContent() {
        synchronized (dictionary) {
            return dictionary;
        }
    }

    @Override
    public IDict<T1, T2> copy() {
        IDict<T1, T2> toReturn = new Dict<>();
        for (T1 key : keySet())
            toReturn.put(key, get(key));
        return toReturn;
    }
}
