package model.values;

import model.types.ReferenceType;
import model.types.Type;

public class ReferenceValue implements Value{
    private final int address;
    private final Type locationType;

    public ReferenceValue(int _address, Type _locationType) {
        address = _address;
        locationType = _locationType;
    }

    public int getAddress() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new ReferenceType(locationType);
    }

    @Override
    public String toString() {
        return String.format("ReferenceValue{%d -> %s}", address, locationType);
    }
}
