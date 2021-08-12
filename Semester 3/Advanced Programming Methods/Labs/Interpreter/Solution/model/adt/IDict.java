package model.adt;

import java.util.Map;
import java.util.Set;

public interface IDict<T1,T2>{
    void put(T1 v1, T2 v2);
    T2 get(T1 id);
    boolean containsKey(T1 id);
    Set<T1> keySet();
    void remove(T1 key);
    Map<T1, T2> getContent();
    IDict<T1, T2> copy();
}
