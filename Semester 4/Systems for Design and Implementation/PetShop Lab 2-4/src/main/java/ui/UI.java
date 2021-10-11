package ui;

import domain.BaseEntity;
import domain.exceptions.PetShopException;
import domain.validators.*;
import repository.csvRepository.*;
import repository.InMemoryRepository;
import repository.databaseRepository.*;
import repository.xmlRepository.*;
import service.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class UI {
    private Service service;
    private final Map<Integer, Runnable> menuTable;

    public UI() {
        menuTable = new HashMap<>();
    }

    private HashMap<String, String> readSettingsFile() {
        HashMap<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        String configFile = "data/programData/settings.properties";
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(configFile);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return propertiesMap;
        }
        Stream.ofNullable(fileInputStream).findAny().ifPresentOrElse((el) ->  {
            try {
                properties.load(fileInputStream);
                propertiesMap.put("database", properties.getProperty("database"));
                propertiesMap.put("user", properties.getProperty("user"));
                propertiesMap.put("password", properties.getProperty("password"));
            } catch (IOException ioException) {
                System.out.println("IOException: " + ioException.getMessage());
            }
        }, () -> System.out.println("Invalid config file"));

        return propertiesMap;
    }
    /**
     * One of the functions that must be called to instantiate the service.
     * It instantiates in memory repositories for each entity
     */
    void initialiseMemoryApplication() {
        service = new Service(
                new InMemoryRepository<>(new CatValidator()),
                new InMemoryRepository<>(new FoodValidator()),
                new InMemoryRepository<>(new CatFoodValidator()),
                new InMemoryRepository<>(new CustomerValidator()),
                new InMemoryRepository<>(new PurchaseValidator())
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
                new CatFoodCSVRepository(new CatFoodValidator(), "data/programData/csvData/catFoods.csv"),
                new CustomerCSVRepository(new CustomerValidator(), "data/programData/csvData/customers.csv"),
                new PurchaseCSVRepository(new PurchaseValidator(), "data/programData/csvData/purchases.csv")
                );
    }

    void initialiseDatabaseApplication() {
        HashMap<String, String> configurations = readSettingsFile();
        service = new Service(
                new CatDatabaseRepository(new CatValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new FoodDatabaseRepository(new FoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new CatFoodDatabaseRepository(new CatFoodValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new CustomerDatabaseRepository(new CustomerValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password")),
                new PurchaseDatabaseRepository(new PurchaseValidator(), configurations.get("database"), configurations.get("user"), configurations.get("password"))
        );
    }

    void initialiseXMLApplication() {
        service = new Service(
                new CatXMLRepository(new CatValidator(), "data/programData/xmlData/cats.xml"),
                new FoodXMLRepository(new FoodValidator(), "data/programData/xmlData/foods.xml"),
                new CatFoodXMLRepository(new CatFoodValidator(), "data/programData/xmlData/catFoods.xml"),
                new CustomerXMLRepository(new CustomerValidator(), "data/programData/xmlData/customers.xml"),
                new PurchaseXMLRepository(new PurchaseValidator(), "data/programData/xmlData/purchases.xml")
                );
    }

    static void printMenu() {
        writeConsole("Welcome to the Wild Cats Pet Shop");
        writeConsole("1. Add a cat");
        writeConsole("2. Add food");
        writeConsole("3. Feed a cat");
        writeConsole("4. Add a customer");
        writeConsole("5. Make a purchase");
        writeConsole("6. Show all cats");
        writeConsole("7. Show all the food");
        writeConsole("8. Show all cat food pairs");
        writeConsole("9. Show all customers");
        writeConsole("10. Show all purchases");
        writeConsole("11. Delete cat");
        writeConsole("12. Delete food");
        writeConsole("13. Stop feeding a cat");
        writeConsole("14. Delete a customer");
        writeConsole("15. Return a purchase");
        writeConsole("16. Update cat");
        writeConsole("17. Update food");
        writeConsole("18. Update cat food");
        writeConsole("19. Update customer");
        writeConsole("20. Change purchase review");
        writeConsole("21. Filter cats that eat a certain food");
        writeConsole("22. Filter customers that bought at least a cat of a certain breed");
        writeConsole("23. Filter purchases based on the minimum number of stars");
        writeConsole("24. Report customers and their spent cash sorted by that amount");
        writeConsole("0. Exit");
    }

    static void printStorageTypes() {
        writeConsole("Welcome to the Wild Cats Pet Shop");
        writeConsole("Choose type of storage");
        writeConsole("1. Memory");
        writeConsole("2. File");
        writeConsole("3. Database");
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
        try {
            int application = readNumberFromConsole();
            Stream.of(applicationStyleTable.get(application))
                    .filter(Objects::nonNull)
                    .findAny()
                    .ifPresentOrElse((val) -> { try {
                        val.run();
                    } catch (Exception exception) {
                        System.out.println("Error on initialising repo: " + exception.getMessage());
                        chooseApplicationStyle();
                    }
            }, () -> {
                        System.out.println("Invalid choice");
                        chooseApplicationStyle();
                    });
        } catch (InputMismatchException inputMismatchException) {
            System.out.println("Invalid integer, try again");
            chooseApplicationStyle();
        }
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
        writeConsole("Breed: ");
        String breed = stdin.next();
        writeConsole("Age (in cat years): ");
        int age = stdin.nextInt();
        service.addCat(id.get(), name, breed, age);
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
            System.out.println("Incorrect date format");
        }
    }

    /**
     * This function adds a customer entity with the input from the user and id as maximum up until now plus 1
     */
    public void addCustomer(){
        Scanner stdin = new Scanner(System.in);
        AtomicReference<Long> id = new AtomicReference<>(0L);
        service.getCustomersFromRepository()
                .stream()
                .max(Comparator.comparingLong(BaseEntity::getId)).ifPresent(customer -> id.set(customer.getId()+1));
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Phone number (10 digits): ");
        String phoneNumber = stdin.next();
        service.addCustomer(id.get(), name, phoneNumber);
    }

    /**
     * This function adds a purchase entity with the input from user and current date
     */
    public void addPurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        writeConsole("Price: ");
        int price = stdin.nextInt();
        writeConsole("Review: ");
        int review = stdin.nextInt();
        service.addPurchase(catId, customerId, price, new Date(), review);
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
     * This function prints the customers to the console
     */
    public void showCustomers(){
        service.getCustomersFromRepository().forEach(customer -> System.out.println(customer.toString()));
    }

    public void showPurchases() {
        service.getPurchasesFromRepository().forEach(purchase -> System.out.println(purchase.toString()));
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
     * This function deletes a CatFood entity
     */
    public void deleteCustomer(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        try{
            service.deleteCustomer(customerId);
        }
        catch (PetShopException petShopException){
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }
    }

    /**
     * This function deletes a purchase entity
     */
    public void deletePurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        service.deletePurchase(catId, customerId);
    }
    /**
     * This function prints the result of the join between cats and foods
     */
    public void showCatFoodPairs(){
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
        writeConsole("Breed: ");
        String breed = stdin.next();
        writeConsole("Age (in cat years): ");
        int age = stdin.nextInt();
        try{
            service.updateCat(id, name, breed, age);
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

    /**
     * This function updates a customer entity
     */
    public void updateCustomer(){
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Phone number (10 digits): ");
        String phoneNumber = stdin.next();
        try{
            service.updateCustomer(id, name, phoneNumber);
        } catch (PetShopException petShopException) {
            petShopException.printStackTrace();
            System.out.println(petShopException.getMessage());
        }

    }

    public void updatePurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Customer Id: ");
        Long customerId = stdin.nextLong();
        writeConsole("Give review: ");
        int review = stdin.nextInt();
        service.updatePurchase(catId, customerId, review);
    }

    public void filterCatsBasedOnFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();

        service.filterCatsThatEatCertainFood(foodId)
                .forEach(cat->writeConsole(cat.toString()));
    }

    public void filterCustomersBasedOnBreedPurchase() {
        writeConsole("Breed: ");
        Scanner stdin = new Scanner(System.in);
        String breed = stdin.next();
        service.filterCustomersThatBoughtBreedOfCat(breed).forEach(customer -> writeConsole(customer.toString()));
    }

    public void filterPurchasesBasedOnReview() {
        writeConsole("Minimum stars: ");
        int minStars = readNumberFromConsole();
        service.filterPurchasesWithMinStars(minStars).forEach(purchase -> System.out.println(purchase.toString()));
    }

    public void reportCustomersSortedBySpentCash() {
        service.reportCustomersSortedBySpentCash().forEach(pair -> System.out.printf("%s\nSpent money: %s\n\n", pair.getLeft(), pair.getRight()));
    }

    /**
     * The main function which processes the input
     */
    public void runProgram() {
        chooseApplicationStyle();

        menuTable.put(1, this::addCat);
        menuTable.put(2, this::addFood);
        menuTable.put(3, this::feedCat);
        menuTable.put(4, this::addCustomer);
        menuTable.put(5, this::addPurchase);
        menuTable.put(6, this::showCats);
        menuTable.put(7, this::showFood);
        menuTable.put(8, this::showCatFoodPairs);
        menuTable.put(9, this::showCustomers);
        menuTable.put(10, this::showPurchases);
        menuTable.put(11, this::deleteCat);
        menuTable.put(12, this::deleteFood);
        menuTable.put(13, this::stopFeedingCat);
        menuTable.put(14, this::deleteCustomer);
        menuTable.put(15, this::deletePurchase);
        menuTable.put(16, this::updateCat);
        menuTable.put(17, this::updateFood);
        menuTable.put(18, this::updateCatFood);
        menuTable.put(19, this::updateCustomer);
        menuTable.put(20, this::updatePurchase);
        menuTable.put(21, this::filterCatsBasedOnFood);
        menuTable.put(22, this::filterCustomersBasedOnBreedPurchase);
        menuTable.put(23, this::filterPurchasesBasedOnReview);
        menuTable.put(24, this::reportCustomersSortedBySpentCash);
        menuTable.put(0, () -> System.exit(0));

        //noinspection EndlessStream
        IntStream.generate(() -> 0)
            .forEach(i -> {
                printMenu();
                try {
                    int choice = readNumberFromConsole();
                    Stream.of(menuTable.get(choice))
                            .filter(Objects::nonNull)
                            .findAny()
                            .ifPresentOrElse(Runnable::run, () -> System.out.println("Bad choice"));
                } catch (InputMismatchException inputMismatchException) {
                    System.out.println("Invalid integer");
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            });
    }
}
