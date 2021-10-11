package repository;

import domain.*;
import domain.exceptions.PetShopException;
import domain.validators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.xmlRepository.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class XMLRepositoryTest {
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

    private static final String catFile = "data/testData/xmlData/cats.xml";
    private static final String foodFile = "data/testData/xmlData/foods.xml";
    private static final String catFoodFile = "data/testData/xmlData/catFoods.xml";
    private static final String customerFile = "data/testData/xmlData/customers.xml";
    private static final String purchaseFile = "data/testData/xmlData/purchases.xml";

    @BeforeEach
    public void setUp() {
        writeToFile(catFile, """
                <?xml version="1.0" encoding="UTF-8" ?>
                <cats>
                    <cat id="1">
                        <name>cat1</name>
                        <breed>o1</breed>
                        <age>5</age>
                    </cat>
                </cats>""");
        writeToFile(foodFile, """
                <?xml version="1.0" encoding="UTF-8" ?>
                <foods>
                    <food id="2">
                        <name>n1</name>
                        <producer>p1</producer>
                        <expirationDate>12-02-1999</expirationDate>
                    </food>
                </foods>""");
        writeToFile(catFoodFile, """
                <?xml version="1.0" encoding="UTF-8" ?>
                <catFoods>
                    <catFood id="1,2"></catFood>
                </catFoods>""");

        writeToFile(customerFile, """
                <?xml version="1.0" encoding="UTF-8" standalone="no"?>
                <customers>
                    <customer id="1">
                        <name>Ana</name>
                        <phoneNumber>0723456789</phoneNumber>
                    </customer>
                </customers>""");

        writeToFile(purchaseFile, """
                <?xml version="1.0" encoding="UTF-8" ?>
                <purchases>
                    <purchase id="1,4">
                        <price>24</price>
                        <dateAcquired>28-10-2000</dateAcquired>
                        <review>4</review>
                    </purchase>
                </purchases>""");
        catsRepository = new CatXMLRepository(new CatValidator(), catFile);
        foodRepository = new FoodXMLRepository(new FoodValidator(), foodFile);
        catFoodRepository = new CatFoodXMLRepository(new CatFoodValidator(), catFoodFile);
        customerRepository = new CustomerXMLRepository(new CustomerValidator(), customerFile);
        purchaseRepository = new PurchaseXMLRepository(new PurchaseValidator(), purchaseFile);

        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 12);
        food.setExpirationDate(calendar.getTime());
        newFood.setExpirationDate(calendar.getTime());

        calendar.set(2000, Calendar.OCTOBER, 28);
        date = calendar.getTime();
    }

    private static void writeToFile(String filePath, String line){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            writer.println(line);
        } catch (IOException e) {
            throw new PetShopException("Invalid file");
        }
    }

    @AfterEach
    public void tearDown() {
        catsRepository = null;
        foodRepository = null;
        catFoodRepository = null;
        customerRepository = null;
        purchaseRepository = null;

        writeToFile(catFile, "");
        writeToFile(foodFile, "");
        writeToFile(catFoodFile, "");
        writeToFile(customerFile, "");
        writeToFile(purchaseFile, "");
    }

    @Test
    void testAddValidCat() {
        catsRepository.save(newCat);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 2);
    }

    @Test
    void testAddExistingCat() {
        catsRepository.save(cat);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testAddValidFood() {
        foodRepository.save(newFood);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 2);
    }
    @Test
    void testAddExistingFood() {
        foodRepository.save(food);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }
    @Test
    void testAddValidCatFood(){
        catFoodRepository.save(new CatFood(newCat.getId(), newFood.getId()));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 2);
    }

    @Test
    void testAddExistentCatFood(){
        catFoodRepository.save(new CatFood(1L, 2L));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testAddValidPurchase() {
        purchaseRepository.save(new Purchase(newCat.getId(), customer.getId(), 6, new Date(), 3));
        AtomicInteger size = new AtomicInteger();
        purchaseRepository.findAll().forEach(purchase -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 2);
    }

    @Test
    void testAddExistingPurchase() {
        purchaseRepository.save(new Purchase(cat.getId(), customer.getId(), 6, new Date(), 3));
        AtomicInteger size = new AtomicInteger();
        purchaseRepository.findAll().forEach(purchase -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidCat(){
        catsRepository.delete(1L);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 0);
    }

    @Test
    void testDeleteNonExistentCat(){
        catsRepository.delete(111L);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidFood(){
        foodRepository.delete(2L);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 0);
    }

    @Test
    void testDeleteNonExistentFood(){
        foodRepository.delete(111L);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidCatFood(){
        catFoodRepository.delete(new Pair<>(1L, 2L));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 0);
    }

    @Test
    void testDeleteNonExistentCatFood(){
        catFoodRepository.delete(new Pair<>(11L,11L));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidPurchase() {
        purchaseRepository.delete(new Pair<>(1L, 4L));
        AtomicInteger size = new AtomicInteger();
        purchaseRepository.findAll().forEach(purchase -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 0);
    }
    @Test
    void testDeleteNonExistentPurchase() {
        purchaseRepository.delete(new Pair<>(11L, 11L));
        AtomicInteger size = new AtomicInteger();
        purchaseRepository.findAll().forEach(purchase -> size.getAndIncrement());
        Assertions.assertEquals(size.get(), 1);
    }

    @Test
    void testUpdateValidCat(){
        catsRepository.update(new Cat(1L, "cat2", "o2", 10));
        Assertions.assertTrue(catsRepository.findOne(1L).isPresent());
        Assertions.assertEquals(catsRepository.findOne(1L).get().getName(), "cat2");
    }

    @Test
    void testUpdateNonExistentCat(){
        catsRepository.update(newCat);
        Assertions.assertFalse(catsRepository.findOne(newCat.getId()).isPresent());
        Assertions.assertTrue(catsRepository.findOne(1L).isPresent());
        Assertions.assertEquals(catsRepository.findOne(1L).get().getName(), "cat1");
    }

    @Test
    void testUpdateValidFood(){
        foodRepository.update(new Food(2L, "food2", "p2", food.getExpirationDate()));
        Assertions.assertTrue(foodRepository.findOne(2L).isPresent());
        Assertions.assertEquals(foodRepository.findOne(2L).get().getName(), "food2");
    }

    @Test
    void testUpdateNonExistentFood(){
        foodRepository.update(newFood);
        Assertions.assertFalse(foodRepository.findOne(newFood.getId()).isPresent());
        Assertions.assertTrue(foodRepository.findOne(2L).isPresent());
        Assertions.assertEquals(foodRepository.findOne(2L).get().getName(), "n1");
    }

    @Test
    void testUpdateValidPurchase() {
        purchaseRepository.update(new Purchase(cat.getId(), customer.getId(), 6, date, 5));
        Assertions.assertTrue(purchaseRepository.findOne(new Pair<>(cat.getId(), customer.getId())).isPresent());
        Assertions.assertEquals(purchaseRepository.findOne(new Pair<>(cat.getId(), customer.getId())).get().getPrice(), 6);
        Assertions.assertEquals(purchaseRepository.findOne(new Pair<>(cat.getId(), customer.getId())).get().getReview(), 5);
    }
    @Test
    void testUpdateNonExistedPurchase() {
        purchaseRepository.update(new Purchase(newCat.getId(), customer.getId(), 6, date, 5));
        Assertions.assertFalse(purchaseRepository.findOne(new Pair<>(newCat.getId(), customer.getId())).isPresent());
        Assertions.assertTrue(purchaseRepository.findOne(new Pair<>(cat.getId(), customer.getId())).isPresent());
        Assertions.assertEquals(purchaseRepository.findOne(new Pair<>(cat.getId(), customer.getId())).get().getPrice(), 24);
        Assertions.assertEquals(purchaseRepository.findOne(new Pair<>(cat.getId(), customer.getId())).get().getReview(), 4);
    }



}
