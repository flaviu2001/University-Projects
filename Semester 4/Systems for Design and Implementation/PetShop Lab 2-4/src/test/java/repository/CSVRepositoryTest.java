package repository;

import domain.*;
import domain.exceptions.PetShopException;
import domain.validators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.csvRepository.*;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CSVRepositoryTest {
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    private IRepository<Long, Customer> customerRepository;
    private IRepository<Pair<Long, Long>, Purchase> purchaseRepository;

    private static Date date;

    private static final Cat cat = new Cat(1L, "cat1", "o1", 5);
    private static final Cat newCat = new Cat(2L, "cat2", "o2", 10);

    private static final Food food = new Food(2L, "n1", "p1", null);
    private static final Food newFood = new Food(5L, "food2", "p2", null);

    private static final Customer customer = new Customer(4L, "Ana", "07TheTestWorks");

    private static final String catFile = "data/testData/csvData/cats.csv";
    private static final String foodFile = "data/testData/csvData/foods.csv";
    private static final String catFoodFile = "data/testData/csvData/catFoods.csv";
    private static final String customerFile = "data/testData/csvData/customers.csv";
    private static final String purchaseFile = "data/testData/csvData/purchases.csv";

    @BeforeEach
    public void setUp(){
        writeToFile(catFile, "1,cat1,o1,5");
        writeToFile(foodFile, "2,n1,p1,12-02-1999");
        writeToFile(catFoodFile, "1,2");
        writeToFile(customerFile, "4,Ana,07TheTestWorks");
        writeToFile(purchaseFile, "1,4,24,28-10-2000,4");


        catsRepository = new CatCSVRepository(new CatValidator(), catFile);
        foodRepository = new FoodCSVRepository(new FoodValidator(), foodFile);
        catFoodRepository = new CatFoodCSVRepository(new CatFoodValidator(), catFoodFile);
        customerRepository = new CustomerCSVRepository(new CustomerValidator(), customerFile);
        purchaseRepository = new PurchaseCSVRepository(new PurchaseValidator(), purchaseFile);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 12);
        food.setExpirationDate(calendar.getTime());
        newFood.setExpirationDate(calendar.getTime());

        calendar.set(2000, Calendar.OCTOBER, 28);
        date = calendar.getTime();
    }

    @AfterEach
    public void tearDown(){
        catFoodRepository = null;
        catsRepository = null;
        foodRepository = null;
        customerRepository = null;
        purchaseRepository = null;
        writeToFile(catFoodFile, "");
        writeToFile(catFile, "");
        writeToFile(foodFile, "");
        writeToFile(customerFile, "");
        writeToFile(purchaseFile, "");
    }

    private static void writeToFile(String filePath, String line){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            writer.println(line);
        } catch (IOException e) {
            throw new PetShopException("Invalid file");
        }
    }

    private static List<String> readFromFile(String filePath){
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new PetShopException(e.getMessage());
        }
    }

    @Test
    void testAddValidCat(){
        catsRepository.save(newCat);
        List<String> lines = readFromFile(catFile);
        Assertions.assertEquals(lines.size(), 2);
    }

    @Test
    void testAddExistentCat(){
        catsRepository.save(cat);
        List<String> lines = readFromFile(catFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,cat1,o1,5");
    }

    @Test
    void testAddValidFood(){
        foodRepository.save(newFood);
        List<String> lines = readFromFile(foodFile);
        Assertions.assertEquals(lines.size(), 2);
    }

    @Test
    void testAddExistentFood(){
        foodRepository.save(food);
        List<String> lines = readFromFile(foodFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "2,n1,p1,12-02-1999");
    }

    @Test
    void testAddValidCatFood(){
        catFoodRepository.save(new CatFood(newCat.getId(), newFood.getId()));
        List<String> lines = readFromFile(catFoodFile);
        Assertions.assertEquals(lines.size(), 2);
    }

    @Test
    void testAddExistentCatFood(){
        catFoodRepository.save(new CatFood(1L, 2L));
        List<String> lines = readFromFile(catFoodFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,2");
    }

    @Test
    void testAddValidPurchase() {
        purchaseRepository.save(new Purchase(newCat.getId(), customer.getId(), 6, new Date(), 3));
        List<String> lines = readFromFile(purchaseFile);
        Assertions.assertEquals(lines.size(), 2);
    }

    @Test
    void testAddExistentPurchase() {
        purchaseRepository.save(new Purchase(cat.getId(), customer.getId(), 6, new Date(), 3));
        List<String> lines = readFromFile(purchaseFile);
        Assertions.assertEquals(lines.size(), 1);
    }
    @Test
    void testDeleteValidCat(){
        catsRepository.delete(1L);
        Assertions.assertEquals(readFromFile(catFile).size(), 0);
    }

    @Test
    void testDeleteNonExistentCat(){
        catsRepository.delete(111L);
        List<String> lines = readFromFile(catFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,cat1,o1,5");
    }

    @Test
    void testDeleteValidFood(){
        foodRepository.delete(2L);
        Assertions.assertEquals(readFromFile(foodFile).size(), 0);
    }

    @Test
    void testDeleteNonExistentFood(){
        foodRepository.delete(111L);
        List<String> lines = readFromFile(foodFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "2,n1,p1,12-02-1999");
    }

    @Test
    void testDeleteValidCatFood(){
        catFoodRepository.delete(new Pair<>(1L, 2L));
        Assertions.assertEquals(readFromFile(catFoodFile).size(), 0);
    }

    @Test
    void testDeleteNonExistentCatFood(){
        catFoodRepository.delete(new Pair<>(11L,11L));
        List<String> lines = readFromFile(catFoodFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,2");
    }

    @Test
    void testDeleteValidPurchase() {
        purchaseRepository.delete(new Pair<>(1L, 4L));
        Assertions.assertEquals(readFromFile(purchaseFile).size(), 0);
    }

    @Test
    void testDeleteNonExistentPurchase() {
        purchaseRepository.delete(new Pair<>(11L,11L));
        List<String> lines = readFromFile(purchaseFile);
        Assertions.assertEquals(lines.size(), 1);
    }

    @Test
    void testUpdateValidCat(){
        catsRepository.update(new Cat(1L, "cat2", "o2", 10));
        List<String> lines = readFromFile(catFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,cat2,o2,10");
    }

    @Test
    void testUpdateNonExistentCat(){
        catsRepository.update(newCat);
        List<String> lines = readFromFile(catFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,cat1,o1,5");
    }

    @Test
    void testUpdateValidFood(){
        foodRepository.update(new Food(2L, "food2", "p2", food.getExpirationDate()));
        List<String> lines = readFromFile(foodFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "2,food2,p2,12-02-1999");
    }

    @Test
    void testUpdateNonExistentFood(){
        foodRepository.update(newFood);
        List<String> lines = readFromFile(foodFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "2,n1,p1,12-02-1999");
    }
    @Test
    void testUpdateValidPurchase() {
        purchaseRepository.update(new Purchase(cat.getId(), customer.getId(), 6, date, 5));
        List<String> lines = readFromFile(purchaseFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,4,6,28-10-2000,5");
    }

    @Test
    void testUpdateNonExistentPurchase() {
        purchaseRepository.update(new Purchase(newCat.getId(), customer.getId(), 6, date, 5));
        List<String> lines = readFromFile(purchaseFile);
        Assertions.assertEquals(lines.size(), 1);
        Assertions.assertEquals(lines.get(0), "1,4,24,28-10-2000,4");
    }

}
