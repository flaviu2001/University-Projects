package controller;

import exceptions.MyException;
import model.IAnimal;
import repository.IRepository;

public class Controller {
    IRepository repository;

    public Controller(IRepository _repository) {
        repository = _repository;
    }

    public void addAnimal(IAnimal animal) throws MyException {
        repository.addObject(animal);
    }

    public void removeAnimal(int position) throws MyException {
        repository.removeObject(position);
    }

    public IAnimal[] filter(int minAge) {
        @SuppressWarnings("MismatchedReadAndWriteOfArray")
        IAnimal[] copy = new IAnimal[repository.getSize()];
        int size = 0;
        for (IAnimal animal : repository.getAllObjects())
            if (animal.getAgeMonths() >= minAge)
                copy[size++] = animal;
        IAnimal[] to_return = new IAnimal[size];
        if (size >= 0) System.arraycopy(copy, 0, to_return, 0, size);
        return to_return;
    }

    public IAnimal[] getAnimals() {
        return repository.getAllObjects();
    }
}
