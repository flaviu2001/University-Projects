package model.types;

import model.values.Value;

public interface Type {
    boolean equals(Type another);
    Value getDefault();
}
