package repository;

import exceptions.MyException;
import model.IAnimal;

public class ArrayRepository implements IRepository {
    IAnimal[] animals;

    int size;

    @Override
    public int getSize() {
        return size;
    }

    public ArrayRepository(int capacity) throws MyException {
        if (capacity <= 0)
            throw new MyException("The capacity of the repository cannot be 0 or less");
        animals = new IAnimal[capacity];
    }

    @Override
    public void addObject(IAnimal animal) throws MyException {
        if (size == animals.length)
            throw new MyException("You have exceeded the size of the repository");
        animals[size++] = animal;
    }

    @Override
    public void removeObject(int position) throws MyException {
        if (position >= size || position < 0)
            throw new MyException("Invalid position.");
        for (int i = position; i+1 < size; ++i)
            animals[i] = animals[i+1];
        --size;
    }

    @Override
    public IAnimal get(int position) throws MyException {
        if (position >= size || position < 0)
            throw new MyException("Invalid position.");
        return animals[position];
    }

    @Override
    public IAnimal[] getAllObjects() {
        IAnimal[] copy = new IAnimal[size];
        if (copy.length >= 0) System.arraycopy(animals, 0, copy, 0, copy.length);
        return copy;
    }
}
