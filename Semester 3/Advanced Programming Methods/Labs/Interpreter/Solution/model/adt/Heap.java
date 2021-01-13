package model.adt;

import exceptions.InterpreterError;
import model.values.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Heap implements IHeap {
    private final Map<Integer, Value> map;
    private Integer freeValue;

    public Integer newValue() {
        Random rand = new Random();
        freeValue = rand.nextInt();
        if (freeValue == 0 || map.containsKey(freeValue))
            freeValue = rand.nextInt();
        return freeValue;
    }

    public Heap(Map<Integer, Value> _map) {
        map = _map;
        freeValue = newValue();
    }

    public Heap() {
        map = new HashMap<>();
        freeValue = newValue();
    }

    @Override
    public Integer getFreeValue() {
        synchronized (this) {
            return freeValue;
        }
    }

    @Override
    public Map<Integer, Value> getContent() {
        synchronized (this) {
            return map;
        }
    }

    @Override
    public void setContent(Map<Integer, Value> newMap) {
        synchronized (this) {
            map.clear();
            for (Integer i : newMap.keySet()) {
                map.put(i, newMap.get(i));
            }
        }
    }

    @Override
    public Integer add(Value value) {
        synchronized (this) {
            map.put(freeValue, value);
            Integer toReturn = freeValue;
            freeValue = newValue();
            return toReturn;
        }
    }

    @Override
    public void update(Integer position, Value value) throws InterpreterError {
        synchronized (this) {
            if (!map.containsKey(position))
                throw new InterpreterError(String.format("ERROR: %d is not present in the heap", position));
            map.put(position, value);
        }
    }

    @Override
    public Value get(Integer position) throws InterpreterError {
        synchronized (this) {
            if (!map.containsKey(position))
                throw new InterpreterError(String.format("ERROR: %s not present in the heap", position));
            return map.get(position);
        }
    }
}
