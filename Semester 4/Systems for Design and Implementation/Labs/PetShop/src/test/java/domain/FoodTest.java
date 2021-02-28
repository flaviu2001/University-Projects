package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

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

    @Before
    public void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 1);
        date = calendar.getTime();
        calendar.set(1999, Calendar.FEBRUARY, 2);
        newDate = calendar.getTime();

        food = new Food(id, name, producer, date);
    }

    @After
    public void tearDown() {
        food = null;
    }

    @Test
    public void testGetName() {
        assertEquals(name, food.getName());
    }

    @Test
    public void testSetName() {
        food.setName(newName);
        assertEquals(newName, food.getName());
    }

    @Test
    public void testGetProducer() {
        assertEquals(producer, food.getProducer());
    }

    @Test
    public void testSetProducer() {
        food.setProducer(producer);
        assertEquals(producer, food.getProducer());
    }


    @Test
    public void testGetDate() {
        assertEquals(date, food.getExpirationDate());
    }

    @Test
    public void testSetDate() {
        food.setExpirationDate(newDate);
        assertEquals(newDate, food.getExpirationDate());
    }

    @Test
    public void testEqualForDifferentFoodsShouldReturnFalse(){
        Food newFood = new Food(newId, newName, newProducer, newDate);
        assertNotEquals(food, newFood);
    }

    @Test
    public void testEqualForSameFoodsShouldReturnTrue(){
        Food newFood = new Food(id, name, producer, date);
        assertEquals(food, newFood);
    }

    @Test
    public void testEqualForDifferentClass(){
        assertNotEquals(food, new BaseEntity<Long>());
    }
}
