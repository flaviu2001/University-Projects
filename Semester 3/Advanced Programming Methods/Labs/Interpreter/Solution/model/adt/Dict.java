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
        dictionary.put(key, value);
    }

    @Override
    public T2 get(T1 key){
        return dictionary.get(key);
    }

    @Override
    public boolean containsKey(T1 id) {
        return dictionary.containsKey(id);
    }

    @Override
    public Set<T1> keySet() {
        return dictionary.keySet();
    }

    @Override
    public void remove(T1 key) {
        dictionary.remove(key);
    }

    @Override
    public void setContent(Map<T1, T2> newDict) {
        dictionary.clear();
        for (T1 i : newDict.keySet()) {
            dictionary.put(i, newDict.get(i));
        }
    }

    @Override
    public Map<T1, T2> getContent() {
        return dictionary;
    }
}
