package ui;

import domain.BaseEntity;
import domain.exceptions.PetShopException;
import domain.validators.CatFoodValidator;
import domain.validators.CatValidator;
import domain.validators.FoodValidator;
import repository.csvRepository.CatCSVRepository;
import repository.csvRepository.CatFoodCSVRepository;
import repository.csvRepository.FoodCSVRepository;
import repository.InMemoryRepository;
import repository.xmlRepository.CatFoodXMLRepository;
import repository.xmlRepository.CatXMLRepository;
import repository.xmlRepository.FoodXMLRepository;
import service.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class UI {
    private Service service;
    private final Map<Integer, Runnable> menuTable;

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

    /**
     * One of the functions that must be called to instantiate the service.
     * It instantiates CSV repositories for each entity
     */
    void initialiseFileApplication() {
        service = new Service(
                new CatCSVRepository(new CatValidator(), "data/programData/csvData/cats.csv"),
                new FoodCSVRepository(new FoodValidator(), "data/programData/csvData/foods.csv"),
                new CatFoodCSVRepository(new CatFoodValidator(), "data/programData/csvData/catFoods.csv")
                );
    }

    void initialiseDatabaseApplication() {
        // TODO
    }

    void initialiseXMLApplication() {
        service = new Service(
                new CatXMLRepository(new CatValidator(), "data/programData/xmlData/cats.xml"),
                new FoodXMLRepository(new FoodValidator(), "data/programData/xmlData/foods.xml"),
                new CatFoodXMLRepository(new CatFoodValidator(), "data/programData/xmlData/catFoods.xml")
                );
    }

    static void printMenu() {
        writeConsole("Welcome to the Wild Cats Pet Shop");
        writeConsole("1. Add a cat");
        writeConsole("2. Add food");
        writeConsole("3. Feed a cat");
        writeConsole("4. Show all cats");
        writeConsole("5. Show all the food");
        writeConsole("6. Show all ");
        writeConsole("7. Delete cat");
        writeConsole("8. Delete food");
        writeConsole("9. Stop feeding a cat");
        writeConsole("10. Update cat");
        writeConsole("11. Update food");
        writeConsole("12. Update cat food");
        writeConsole("13. Filter cats that eat a certain food");
        writeConsole("0. Exit");
    }

    static void printStorageTypes() {
        writeConsole("Welcome to the Wild Cats Pet Shop");
        writeConsole("Choose type of storage");
        writeConsole("1. Memory");
        writeConsole("2. File");
        writeConsole("3. Database - in progress");
        writeConsole("4. XML File");
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
        applicationStyleTable.put(4, this::initialiseXMLApplication);
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
     * This function deletes a cat entity
     */
    public void deleteCat(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        try{
            service.deleteCat(catId);
        }
        catch (PetShopException petShopException){
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }
    }

    /**
     * This function deletes a food entity
     */
    public void deleteFood(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        try{
            service.deleteFood(foodId);
        }
        catch (PetShopException petShopException){
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }
    }

    /**
     * This function deletes a CatFood entity
     */
    public void stopFeedingCat() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        try {
            service.deleteCatFood(catId, foodId);
        } catch (PetShopException petShopException) {
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }
    }

    /**
     * This function prints the result of the join between cats and foods
     */
    public void showAll(){
        service.getCatFoodJoin().forEach(
                catFoodPair -> writeConsole(catFoodPair.getLeft() + "\n" + catFoodPair.getRight() + "\n")
        );
    }

    /**
     * This function updates a cat
     */
    public void updateCat(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Owner: ");
        String owner = stdin.next();
        writeConsole("Age (in cat years): ");
        int age = stdin.nextInt();
        try{
            service.updateCat(id, name, owner, age);
        } catch (PetShopException petShopException) {
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }
    }

    /**
     * This function updates a food
     */
    public void updateFood(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Producer: ");
        String producer = stdin.next();
        writeConsole("Expiration date (dd-mm-yyyy): ");
        String dateString = stdin.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            service.updateFood(id, name, producer, date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            System.out.println("Could not add");
        } catch (PetShopException petShopException) {
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }
    }

    /**
     * This function updates a cat food
     */
    public void updateCatFood(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        writeConsole("New food id: ");
        Long newFoodId = stdin.nextLong();
        try {
            service.updateCatFood(catId, foodId, newFoodId);
        } catch (PetShopException petShopException) {
            petShopException.printStackTrace();
            System.out.println("Could not feed the cat");
        }
    }

    public void filterCatsBasedOnFood(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();

        service.filterCatsThatEatCertainFood(foodId)
                .forEach(cat->writeConsole(cat.toString()));
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
        menuTable.put(6, this::showAll);
        menuTable.put(7, this::deleteCat);
        menuTable.put(8, this::deleteFood);
        menuTable.put(9, this::stopFeedingCat);
        menuTable.put(10, this::updateCat);
        menuTable.put(11, this::updateFood);
        menuTable.put(12, this::updateCatFood);
        menuTable.put(13, this::filterCatsBasedOnFood);
        menuTable.put(0, () -> System.exit(0));

        //noinspection InfiniteLoopStatement
        while (true) {
            printMenu();
            int choice = readNumberFromConsole();
            if(menuTable.containsKey(choice))
                menuTable.get(choice).run();
            else
                System.out.println("Bad choice");
        }
    }
}
