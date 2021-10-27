package view;

import controller.Controller;
import exceptions.MyException;
import model.Fish;
import model.IAnimal;
import model.Turtle;
import repository.ArrayRepository;
import repository.IRepository;

public class View {
    public static void main(String[] args) throws MyException {
        IRepository repository;
        try{
            repository = new ArrayRepository(3);
        }catch (MyException me) {
            System.out.println(me.getMessage());
            return;
        }
        Controller controller = new Controller(repository);
        Fish f1 = new Fish(10, 12);
        Turtle t1 = new Turtle(12, 100);
        Turtle t2 = new Turtle(14, 29);
        Fish f2 = new Fish(1, 1000);
        try {
            controller.addAnimal(f1);
            controller.addAnimal(t1);
            controller.addAnimal(t2);
            controller.addAnimal(f2);
        } catch (MyException me) {
            System.out.println(me.getMessage());
        }
        controller.removeAnimal(1);
        System.out.println("Showing animals after removal.");
        for (IAnimal animal : controller.getAnimals())
            System.out.println(animal.toString());
        System.out.println("Showing filtered animals.");
        for (IAnimal animal : controller.filter(12))
            System.out.println(animal.toString());
    }
}
