package client.ui;


import common.service.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UI {
    private final Map<Integer, Runnable> menuTable;
    private final ICatService catService;
    private final IFoodService foodService;
    private final ICatFoodService catFoodService;
    private final ICustomerService customerService;
    private final IPurchaseService purchaseService;

    public UI(ICatService catService, IFoodService foodService, ICatFoodService catFoodService, ICustomerService customerService, IPurchaseService purchaseService) {
        menuTable = new HashMap<>();
        this.catService = catService;
        this.foodService = foodService;
        this.catFoodService = catFoodService;
        this.customerService = customerService;
        this.purchaseService = purchaseService;
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

    int readNumberFromConsole() {
        Scanner stdin = new Scanner(System.in);
        return stdin.nextInt();
    }

    static void writeConsole(String message) {
        System.out.println(message);
    }

    /**
     * This function takes the input from the user to add a cat with id as maximum up until now plus 1
     */
    void addCat() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Breed: ");
        String breed = stdin.next();
        writeConsole("Age (in cat years): ");
        int age = stdin.nextInt();
        catService.addCat(name, breed, age);
    }

    /**
     * This function adds a food entity with the input from the user and id as maximum up until now plus 1
     */
    void addFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Producer: ");
        String producer = stdin.next();
        writeConsole("Expiration date (dd-mm-yyyy): ");
        String dateString = stdin.next();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            foodService.addFood(name, producer, date);
        } catch (ParseException e) {
            System.out.println("Wrong date format");
        }
    }

    /**
     * This function adds a customer entity with the input from the user and id as maximum up until now plus 1
     */
    public void addCustomer() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Phone number (10 digits): ");
        String phoneNumber = stdin.next();
        customerService.addCustomer(name, phoneNumber);
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
        purchaseService.addPurchase(catId, customerId, price, new Date(), review);
    }


    /**
     * This function prints the food to the console
     */
    public void showFood() {
        foodService.getFoodFromRepository().forEach(System.out::println);
    }

    /**
     * This function prints the cats to the console
     */
    public void showCats() {
        catService.getCatsFromRepository().forEach(System.out::println);
    }

    /**
     * This function prints the customers to the console
     */
    public void showCustomers() {
        customerService.getCustomersFromRepository().forEach(System.out::println);
    }

    public void showPurchases() {
        purchaseService.getPurchasesFromRepository().forEach(System.out::println);
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
        catFoodService.addCatFood(catId, foodId);
    }


    /**
     * This function deletes a cat entity
     */
    public void deleteCat() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        catService.deleteCat(catId);
    }

    /**
     * This function deletes a food entity
     */
    public void deleteFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        foodService.deleteFood(foodId);
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
        catFoodService.deleteCatFood(catId, foodId);
    }

    /**
     * This function deletes a CatFood entity
     */
    public void deleteCustomer() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Customer id: ");
        Long customerId = stdin.nextLong();
        customerService.deleteCustomer(customerId);
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
        purchaseService.deletePurchase(catId, customerId);
    }

    /**
     * This function prints the result of the join between cats and foods
     */
    public void showCatFoodPairs() {
        catFoodService.getCatFoodJoin().forEach(catFoodPair -> {
            System.out.println(catFoodPair.getLeft());
            System.out.println(catFoodPair.getRight() + "\n");
        });
    }


    /**
     * This function updates a cat
     */
    public void updateCat() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Breed: ");
        String breed = stdin.next();
        writeConsole("Age (in cat years): ");
        int age = stdin.nextInt();
        catService.updateCat(id, name, breed, age);
    }

    /**
     * This function updates a food
     */
    public void updateFood() {
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
            foodService.updateFood(id, name, producer, date);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            System.out.println("Could not add");
        }
    }

    /**
     * This function updates a cat food
     */
    public void updateCatFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();
        writeConsole("New food id: ");
        Long newFoodId = stdin.nextLong();
        catFoodService.updateCatFood(catId, foodId, newFoodId);
    }

    /**
     * This function updates a customer entity
     */
    public void updateCustomer() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Id: ");
        Long id = stdin.nextLong();
        writeConsole("Name: ");
        String name = stdin.next();
        writeConsole("Phone number (10 digits): ");
        String phoneNumber = stdin.next();
        customerService.updateCustomer(id, name, phoneNumber);
    }


    public void updatePurchase() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Cat id: ");
        Long catId = stdin.nextLong();
        writeConsole("Customer Id: ");
        Long customerId = stdin.nextLong();
        writeConsole("Give review: ");
        int review = stdin.nextInt();
        purchaseService.updatePurchase(catId, customerId, review);
    }

    public void filterCatsBasedOnFood() {
        Scanner stdin = new Scanner(System.in);
        writeConsole("Food id: ");
        Long foodId = stdin.nextLong();

        catFoodService.filterCatsThatEatCertainFood(foodId).forEach(System.out::println);

    }

    public void filterCustomersBasedOnBreedPurchase() {
        writeConsole("Breed: ");
        Scanner stdin = new Scanner(System.in);
        String breed = stdin.next();

        purchaseService.filterCustomersThatBoughtBreedOfCat(breed).forEach(System.out::println);
    }

    public void filterPurchasesWithMinStars() {
        writeConsole("Minimum stars: ");
        int minStars = readNumberFromConsole();
        purchaseService.filterPurchasesWithMinStars(minStars).forEach(System.out::println);
    }

    public void reportCustomersSortedBySpentCash() {
        purchaseService.reportCustomersSortedBySpentCash().forEach(System.out::println);
    }

    /**
     * The main function which processes the input
     */
    public void runProgram() {
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
        menuTable.put(23, this::filterPurchasesWithMinStars);
        menuTable.put(24, this::reportCustomersSortedBySpentCash);

        while (true) {
            printMenu();
            try {
                int choice = readNumberFromConsole();
                if (choice == 0)
                    break;
                Runnable toRun = menuTable.get(choice);
                if (toRun == null) {
                    System.out.println("Bad choice");
                    continue;
                }
                toRun.run();
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Invalid integer");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
