package domain;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class FoodTest {
    private static final Long id = 1L;
    private static final Long newId = 2L;
    private static final String name = "n1";
    private static final String newName = "n2";
    private static final String producer = "p1";
    private static final String newProducer = "p2";
    private static Date date;
    private static Date newDate;

    private Food food;

    @BeforeEach
    public void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 1);
        date = calendar.getTime();
        calendar.set(1999, Calendar.FEBRUARY, 2);
        newDate = calendar.getTime();

        food = new Food(id, name, producer, date);
    }

    @AfterEach
    public void tearDown() {
        food = null;
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(name, food.getName());
    }

    @Test
    public void testSetName() {
        food.setName(newName);
        Assertions.assertEquals(newName, food.getName());
    }

    @Test
    public void testGetProducer() {
        Assertions.assertEquals(producer, food.getProducer());
    }

    @Test
    public void testSetProducer() {
        food.setProducer(producer);
        Assertions.assertEquals(producer, food.getProducer());
    }


    @Test
    public void testGetDate() {
        Assertions.assertEquals(date, food.getExpirationDate());
    }

    @Test
    public void testSetDate() {
        food.setExpirationDate(newDate);
        Assertions.assertEquals(newDate, food.getExpirationDate());
    }

    @Test
    public void testEqualForDifferentFoodsShouldReturnFalse(){
        Food newFood = new Food(newId, newName, newProducer, newDate);
        Assertions.assertNotEquals(food, newFood);
    }

    @Test
    public void testEqualForSameFoodsShouldReturnTrue(){
        Food newFood = new Food(id, name, producer, date);
        Assertions.assertEquals(food, newFood);
    }

    @Test
    public void testEqualForDifferentClass(){
        Assertions.assertNotEquals(food, new BaseEntity<Long>());
    }
}
