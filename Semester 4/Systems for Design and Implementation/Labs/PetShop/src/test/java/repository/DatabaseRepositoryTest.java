package repository;

import domain.Customer;
import domain.Food;
import domain.exceptions.PetShopException;
import domain.validators.CustomerValidator;
import domain.validators.FoodValidator;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.databaseRepository.CustomerDatabaseRepository;
import repository.databaseRepository.FoodDatabaseRepository;

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

    private static final Customer customer = new Customer(1L, "c1", "0799999999");
    private static final Customer invalidCustomer = new Customer(1L, "c1", "079999999");
    private static final Customer newCustomer = new Customer(2L, "c2", "0799999998");
    private static final Food food = new Food(1L, "f1", "p1", new Date());
    private static final Food invalidFood = new Food(1L, "c1", "", new Date());
    private static final Food newFood = new Food(2L, "c2", "p2", new Date());


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

    @BeforeEach
    public void setUp(){
        HashMap<String, String> configurations = readSettingsFile();

        customerRepository = new CustomerDatabaseRepository(new CustomerValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password"));
        foodRepository = new FoodDatabaseRepository(new FoodValidator(),
                configurations.get("database"),
                configurations.get("user"),
                configurations.get("password"));
    }

    @AfterEach
    public void tearDown(){
        HashMap<String, String> configurations = readSettingsFile();

        String clearCustomersTableCommand = "DROP TABLE IF EXISTS Customers; DROP TABLE IF EXISTS Food; DROP TABLE IF EXISTS Cats";
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
    public void testAddInvalidCustomer(){
        Assertions.assertThrows(PetShopException.class, () -> customerRepository.save(invalidCustomer));
    }

    @Test
    public void testAddInvalidFood() {
        Assertions.assertThrows(PetShopException.class, () -> foodRepository.save(invalidFood));
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
    public void testDeleteNonExistentCustomer(){
        Assertions.assertThrows(NullPointerException.class, () -> customerRepository.delete(2L));
    }

    @Test
    public void testDeleteNonExistentFood() {
        Assertions.assertThrows(NullPointerException.class, () -> foodRepository.delete(2L));
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
}
