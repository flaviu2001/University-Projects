package repository;

import domain.Cat;
import domain.CatFood;
import domain.Food;
import domain.Pair;
import domain.exceptions.PetShopException;
import domain.validators.CatFoodValidator;
import domain.validators.CatValidator;
import domain.validators.FoodValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.xmlRepository.CatFoodXMLRepository;
import repository.xmlRepository.CatXMLRepository;
import repository.xmlRepository.FoodXMLRepository;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class XMLRepositoryTest {
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;

    private static final Cat cat = new Cat(1L, "cat1", "o1", 5);
    private static final Cat newCat = new Cat(2L, "cat2", "o2", 10);

    private static final Food food = new Food(2L, "n1", "p1", null);
    private static final Food newFood = new Food(5L, "food2", "p2", null);

    private static final String catFile = "data/testData/xmlData/cats.xml";
    private static final String foodFile = "data/testData/xmlData/foods.xml";
    private static final String catFoodFile = "data/testData/xmlData/catFoods.xml";

    @BeforeEach
    public void setUp() {
        writeToFile(catFile, """
                <?xml version="1.0" encoding="UTF-8" ?>
                <cats>
                    <cat id="1">
                        <name>cat1</name>
                        <owner>o1</owner>
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

        catsRepository = new CatXMLRepository(new CatValidator(), catFile);
        foodRepository = new FoodXMLRepository(new FoodValidator(), foodFile);
        catFoodRepository = new CatFoodXMLRepository(new CatFoodValidator(), catFoodFile);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 12);
        food.setExpirationDate(calendar.getTime());
        newFood.setExpirationDate(calendar.getTime());
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
        writeToFile(catFile, "");
        writeToFile(foodFile, "");
        writeToFile(catFoodFile, "");
    }

    @Test
    void testAddValidCat() {
        catsRepository.save(newCat);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 2);
    }

    @Test
    void testAddExistingCat() {
        catsRepository.save(cat);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 1);
    }

    @Test
    void testAddValidFood() {
        foodRepository.save(newFood);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 2);
    }
    @Test
    void testAddExistingFood() {
        foodRepository.save(food);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 1);
    }
    @Test
    void testAddValidCatFood(){
        catFoodRepository.save(new CatFood(newCat.getId(), newFood.getId()));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 2);
    }

    @Test
    void testAddExistentCatFood(){
        catFoodRepository.save(new CatFood(1L, 2L));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidCat(){
        catsRepository.delete(1L);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 0);
    }

    @Test
    void testDeleteNonExistentCat(){
        catsRepository.delete(111L);
        AtomicInteger size = new AtomicInteger();
        catsRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidFood(){
        foodRepository.delete(2L);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 0);
    }

    @Test
    void testDeleteNonExistentFood(){
        foodRepository.delete(111L);
        AtomicInteger size = new AtomicInteger();
        foodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 1);
    }

    @Test
    void testDeleteValidCatFood(){
        catFoodRepository.delete(new Pair<>(1L, 2L));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 0);
    }

    @Test
    void testDeleteNonExistentCatFood(){
        catFoodRepository.delete(new Pair<>(11L,11L));
        AtomicInteger size = new AtomicInteger();
        catFoodRepository.findAll().forEach(cat -> size.getAndIncrement());
        assertEquals(size.get(), 1);
    }

    @Test
    void testUpdateValidCat(){
        catsRepository.update(new Cat(1L, "cat2", "o2", 10));
        assertEquals(catsRepository.findOne(1L).isPresent(), true);
        assertEquals(catsRepository.findOne(1L).get().getName(), "cat2");
    }

    @Test
    void testUpdateNonExistentCat(){
        catsRepository.update(newCat);
        assertEquals(catsRepository.findOne(newCat.getId()).isPresent(), false);
        assertEquals(catsRepository.findOne(1L).isPresent(), true);
        assertEquals(catsRepository.findOne(1L).get().getName(), "cat1");
    }

    @Test
    void testUpdateValidFood(){
        foodRepository.update(new Food(2L, "food2", "p2", food.getExpirationDate()));
        assertEquals(foodRepository.findOne(2L).isPresent(), true);
        assertEquals(foodRepository.findOne(2L).get().getName(), "food2");
    }

    @Test
    void testUpdateNonExistentFood(){
        foodRepository.update(newFood);
        assertEquals(foodRepository.findOne(newFood.getId()).isPresent(), false);
        assertEquals(foodRepository.findOne(2L).isPresent(), true);
        assertEquals(foodRepository.findOne(2L).get().getName(), "n1");
    }




}
