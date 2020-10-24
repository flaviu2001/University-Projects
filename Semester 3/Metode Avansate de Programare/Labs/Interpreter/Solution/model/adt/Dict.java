package model.adt;
import exceptions.InterpreterError;

import java.util.HashMap;

public class Dict<T1,T2> implements IDict<T1,T2> {
    HashMap<T1, T2> dictionary;

    public Dict() {
        dictionary = new HashMap<>();
    }

    @Override
    public void add(T1 key, T2 value) throws InterpreterError {
        if (dictionary.containsKey(key))
            throw new InterpreterError(String.format("ERROR: Key %s is already present in the symTable", key.toString()));
        dictionary.put(key, value);
    }

    @Override
    public void update(T1 key, T2 value) throws InterpreterError {
        if (!dictionary.containsKey(key))
            throw new InterpreterError(String.format("ERROR: Key %s is not present in the symTable", key.toString()));
        dictionary.put(key, value);
    }

    @Override
    public T2 lookup(T1 key) throws InterpreterError {
        if (!dictionary.containsKey(key))
            throw new InterpreterError(String.format("ERROR: Key %s not found", key.toString()));
        return dictionary.get(key);
    }

    @Override
    public boolean isDefined(T1 id) {
        return dictionary.containsKey(id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (T1 key : dictionary.keySet()) {
            stringBuilder.append(String.format("%s:%s\n", key.toString(), dictionary.get(key).toString()));
        }
        return stringBuilder.toString();
    }
}
