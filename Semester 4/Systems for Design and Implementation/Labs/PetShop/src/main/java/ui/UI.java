package ui;

import domain.BaseEntity;
import domain.exceptions.PetShopException;
import domain.validators.CatFoodValidator;
import domain.validators.CatValidator;
import domain.validators.FoodValidator;
import repository.InMemoryRepository;
import service.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class UI {
    Service service;
    Map<Integer, Runnable> menuTable;

    public UI() {
        menuTable = new HashMap<>();
    }

    /**
     * One of the functions that must be called to instantiate the service.
     * It instantiates in memory repositories for each entity
     */
    void initialiseMemoryApplication() {
        service = new Service(
                new InMemoryRepository<>(new CatValidator()),
                new InMemoryRepository<>(new FoodValidator()),
                new InMemoryRepository<>(new CatFoodValidator())
        );
    }

    void initialiseFileApplication() {
        // TODO
    }

    void initialiseDatabaseApplication() {
        // TODO
    }

    static void printMenu() {
        writeConsole("Welcome to the Wild Cats Pet Shop");
        writeConsole("1. Add a cat");
        writeConsole("2. Add food");
        writeConsole("3. Feed a cat");
        writeConsole("4. Show all cats");
        writeConsole("5. Show all the food");
        writeConsole("6. Show all ");
        writeConsole("0. Exit");
    }

    static void printStorageTypes() {
        writeConsole("Welcome to the Wild Cats Pet Shop");
        writeConsole("Choose type of storage");
        writeConsole("1. Memory");
        writeConsole("2. File - in progress");
        writeConsole("3. Database - in progress");
        writeConsole("0. Exit");
    }

    int readNumberFromConsole() {
        Scanner stdin = new Scanner(System.in);
        return stdin.nextInt();
    }

    static void writeConsole(String message) {
        System.out.println(message);
    }

    /**
     * This function upon call makes the user choose the repositories for the service
     */
    void chooseApplicationStyle() {
        printStorageTypes();
        Map<Integer, Runnable> applicationStyleTable = new HashMap<>();
        applicationStyleTable.put(1, this::initialiseMemoryApplication);
        applicationStyleTable.put(2, this::initialiseFileApplication);
        applicationStyleTable.put(3, this::initialiseDatabaseApplication);
        applicationStyleTable.put(0, () -> System.exit(0));
        int application = readNumberFromConsole();
        applicationStyleTable.get(application).run();
    }

    /**
     * This function takes the input from the user to add a cat with id as maximum up until now plus 1
     */
    void addCat() {
        Scanner stdin = new Scanner(System.in);
        AtomicReference<Long> id = new AtomicReference<>(0L);
        service.getCatsFromRepository()
                .stream()
                .max(Comparator.comparingLong(BaseEntity::getId)).ifPresent(cat -> id.set(cat.getId()+1));
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Owner: ");
        String owner = stdin.next();
        writeConsole("Age (in cat years): ");
        int age = stdin.nextInt();
        service.addCat(id.get(), name, owner, age);
    }

    /**
     * This function adds a food entity with the input from the user and id as maximum up until now plus 1
     */
    void addFood() {
        Scanner stdin = new Scanner(System.in);
        AtomicReference<Long> id = new AtomicReference<>(0L);
        service.getFoodFromRepository()
                .stream()
                .max(Comparator.comparingLong(BaseEntity::getId)).ifPresent(food -> id.set(food.getId()+1));
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Producer: ");
        String producer = stdin.next();
        writeConsole("Expiration date (dd-mm-yyyy): ");
        String dateString = stdin.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            service.addFood(id.get(), name, producer, date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            System.out.println("Could not add");
        }
    }

    /**
     * This function prints the food to the console
     */
    public void showFood() {
        service.getFoodFromRepository().forEach(food -> System.out.println(food.toString()));
    }

    /**
     * This function prints the cats to the console
     */
    public void showCats() {
        service.getCatsFromRepository().forEach(cat -> System.out.println(cat.toString()));
    }

    /**
     * This function adds a CatFood entity
     */
    public void feedCat() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        try {
            service.addCatFood(catId, foodId);
        } catch (PetShopException petShopException) {
            petShopException.printStackTrace();
            System.out.println("Could not feed the cat");
        }
    }

    /**
     * The main function which processes the input
     */
    public void runProgram() {
        chooseApplicationStyle();

        menuTable.put(1, this::addCat);
        menuTable.put(2, this::addFood);
        menuTable.put(3, this::feedCat);
        menuTable.put(4, this::showCats);
        menuTable.put(5, this::showFood);
        menuTable.put(0, () -> System.exit(0));

        //noinspection InfiniteLoopStatement
        while (true) {
            printMenu();
            int choice = readNumberFromConsole();
            menuTable.get(choice).run();
        }
    }
}
