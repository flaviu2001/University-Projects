package repository;

import exceptions.MyException;
import model.IAnimal;

public interface IRepository {
    void addObject(IAnimal animal) throws MyException;
    void removeObject(int position) throws MyException;
    IAnimal get(int position) throws MyException;
    IAnimal[] getAllObjects();
    int getSize();
}
