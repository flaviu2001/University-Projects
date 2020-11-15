package model.adt;

import exceptions.InterpreterError;
import model.values.Value;

import java.util.Map;

public interface IHeap {
    Integer getFreeValue();
    Map<Integer, Value> getContent();
    void setContent(Map<Integer, Value> newMap);
    Integer add(Value value);
    void update(Integer position, Value value) throws InterpreterError;
    Value get(Integer position) throws InterpreterError;
}
