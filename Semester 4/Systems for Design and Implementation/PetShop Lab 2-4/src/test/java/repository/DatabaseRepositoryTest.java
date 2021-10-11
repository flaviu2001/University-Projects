package repository;

import domain.*;
import domain.exceptions.PetShopException;
import domain.validators.*;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.databaseRepository.*;
import repository.xmlRepository.CatXMLRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DatabaseRepositoryTest {
    private IRepository<Long, Customer> customerRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Pair<Long, Long>, Purchase> purchaseRepository;

    private static final Customer customer = new Customer(1L, "c1", "0799999999");
    private static final Customer invalidCustomer = new Customer(1L, "c1", "079999999");
    private static final Customer newCustomer = new Customer(2L, "c2", "0799999998");
    private static final Food food = new Food(1L, "f1", "p1", new Date());
    private static final Food invalidFood = new Food(1L, "c1", "", new Date());
    private static final Food newFood = new Food(2L, "c2", "p2", new Date());
    private static final CatFood catFood = new CatFood(1L, 1L);
    private static final Cat cat = new Cat(1L, "cat1", "o1", 5);
    private static final Cat newCat = new Cat(2L, "cat2", "o2", 10);
    private static final Cat invalidCat = new Cat(1L, "cat1", "o1", 5);
    private static final Purchase purchase = new Purchase(1L, 1L, 10, new Date(), 5);

    private static HashMap<String, String> readSettingsFile() {
        HashMap<String, String> propertiesMap = new HashMap<>();
        Properties properties = new Properties();

        String configFile = "data/testData/settings.properties";
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

    private AtomicInteger getCustomersRepositorySize(){
        AtomicInteger size = new AtomicInteger();
        customerRepository.findAll().forEach(customer -> size.getAndIncrement());
        return size;
    }

    private AtomicInteger getFoodRepositorySize() {
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(food1 -> size.getAndIncrement());
        return size;
    }

    private AtomicInteger getCatFoodRepositorySize() {
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(catFood -> size.getAndIncrement());
        return size;
    }

    private AtomicInteger getCatRepositorySize() {
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        return size;
    }

    private AtomicInteger getPurchaseRepositorySize() {
        AtomicInteger size = new AtomicInteger();
        purchaseRepository.findAll().forEach(purchase -> size.getAndIncrement());
        return size;
    }

    @BeforeEach
    public void setUp(){
        HashMap<String, String> configurations = readSettingsFile();

        catsRepository = new CatDatabaseRepository(new CatValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password"));
        customerRepository = new CustomerDatabaseRepository(new CustomerValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password"));
        foodRepository = new FoodDatabaseRepository(new FoodValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password"));
        catFoodRepository = new CatFoodDatabaseRepository(
                new CatFoodValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password")
        );
        purchaseRepository = new PurchaseDatabaseRepository(
                new PurchaseValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password")
        );
    }

    @AfterEach
    public void tearDown(){
        HashMap<String, String> configurations = readSettingsFile();

        String clearCustomersTableCommand = "DROP TABLE IF EXISTS Customers; DROP TABLE IF EXISTS Food; DROP TABLE IF EXISTS Cats;" +
                "DROP TABLE IF EXISTS CatFoods;" +
                "DROP TABLE IF EXISTS Purchases";
        try (var connection = DriverManager.getConnection(configurations.get("database"),
                configurations.get("user"),
                configurations.get("password"));
             var preparedStatement = connection.prepareStatement(clearCustomersTableCommand)
        ){
            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        customerRepository = null;
        foodRepository = null;
        catsRepository = null;
        catFoodRepository = null;
        purchaseRepository = null;
    }

    @Test
    public void testAddValidCustomer(){
        customerRepository.save(customer);
        Assertions.assertEquals(getCustomersRepositorySize().get(), 1);
    }

    @Test
    public void testAddValidFood() {
        foodRepository.save(food);
        Assertions.assertEquals(getFoodRepositorySize().get(), 1);
    }

    @Test
    public void testAddValidCatFood(){
        catFoodRepository.save(catFood);
        Assertions.assertEquals(getCatFoodRepositorySize().get(), 1);
    }

    @Test
    void testAddValidCat() {
        catsRepository.save(newCat);
        Assertions.assertEquals(getCatRepositorySize().get(), 1);

    }

    @Test
    public void testAddValidPurchase(){
        purchaseRepository.save(purchase);
        Assertions.assertEquals(getPurchaseRepositorySize().get(), 1);
    }

    @Test
    public void testAddInvalidCustomer(){
        Assertions.assertThrows(PetShopException.class, () -> customerRepository.save(invalidCustomer));
    }

    @Test
    public void testAddInvalidFood() {
        Assertions.assertThrows(PetShopException.class, () -> foodRepository.save(invalidFood));
    }

    @Test
    public void testAddInvalidCatFood() {
        catFoodRepository.save(catFood);
        Assertions.assertThrows(PetShopException.class, () -> catFoodRepository.save(catFood));
    }

    @Test
    public void testAddInvalidCat() {
        catsRepository.save(cat);
        Assertions.assertThrows(PetShopException.class, () -> catsRepository.save(invalidCat));
    }

    @Test
    public void testAddInvalidPurchase() {
        Assertions.assertThrows(PetShopException.class, () -> purchaseRepository.save(new Purchase(1L, 1L, -1, new Date(), 4)));
    }

    @Test
    public void testDeleteValidCustomer(){
        customerRepository.save(customer);
        customerRepository.delete(customer.getId());
        Assertions.assertEquals(getCustomersRepositorySize().get(), 0);
    }

    @Test
    public void testDeleteValidFood() {
        foodRepository.save(food);
        foodRepository.delete(food.getId());
        Assertions.assertEquals(getFoodRepositorySize().get(), 0);
    }

    @Test
    public void testDeleteValidCatFood() {
        catFoodRepository.save(catFood);
        catFoodRepository.delete(catFood.getId());
        Assertions.assertEquals(getCatFoodRepositorySize().get(), 0);
    }

    @Test
    void testDeleteValidCat(){
        catsRepository.save(cat);
        catsRepository.delete(cat.getId());
        Assertions.assertEquals(getCatRepositorySize().get(), 0);
    }

    @Test
    public void testDeleteValidPurchase() {
        purchaseRepository.save(purchase);
        purchaseRepository.delete(purchase.getId());
        Assertions.assertEquals(getPurchaseRepositorySize().get(), 0);
    }

    @Test
    public void testDeleteNonExistentCustomer(){
        Assertions.assertThrows(NullPointerException.class, () -> customerRepository.delete(2L));
    }

    @Test
    public void testDeleteNonExistentFood() {
        Assertions.assertThrows(NullPointerException.class, () -> foodRepository.delete(2L));
    }

    @Test
    public void testDeleteNonExistentCatFood() {
        Assertions.assertThrows(NullPointerException.class, () -> catFoodRepository.delete(new Pair<>(1L, 1L)));
    }

    @Test
    void testDeleteNonExistentCat(){
        Assertions.assertThrows(NullPointerException.class, () -> catsRepository.delete(cat.getId()));
    }

    @Test
    public void testDeleteNonExistentPurchase() {
        Assertions.assertThrows(NullPointerException.class, () -> catFoodRepository.delete(new Pair<>(1L, 1L)));
    }

    @Test
    public void testUpdateValidCustomer(){
        customerRepository.save(customer);
        customerRepository.update(new Customer(1L, "c100", "0799999999"));
        Assertions.assertTrue(customerRepository.findOne(1L).isPresent());
        Assertions.assertEquals(customerRepository.findOne(1L).get().getName(), "c100");
    }

    @Test
    public void testUpdateValidFood() {
        foodRepository.save(food);
        foodRepository.update(new Food(1L, "f100", "p100", new Date()));
        Assertions.assertTrue(foodRepository.findOne(1L).isPresent());
        Assertions.assertEquals(foodRepository.findOne(1L).get().getName(), "f100");
    }

    @Test
    public void testUpdateValidCatFood(){
        catFoodRepository.save(catFood);
        catFoodRepository.update(new CatFood(1L, 2L));
        Assertions.assertTrue(catFoodRepository.findOne(new Pair<>(1L, 2L)).isPresent());
    }

    @Test
    void testUpdateValidCat(){
        catsRepository.save(cat);
        catsRepository.update(new Cat(1L, "cat2", "o2", 10));
        Assertions.assertTrue(catsRepository.findOne(1L).isPresent());
        Assertions.assertEquals(catsRepository.findOne(1L).get().getName(), "cat2");
    }

    @Test
    public void testUpdateValidPurchase(){
        purchaseRepository.save(purchase);
        purchaseRepository.update(new Purchase(1L, 1L, 10, new Date(), 3));
        Assertions.assertTrue(purchaseRepository.findOne(new Pair<>(1L, 1L)).isPresent());
        Assertions.assertEquals(purchaseRepository.findOne(new Pair<>(1L, 1L)).get().getReview(),  3);
    }
}
