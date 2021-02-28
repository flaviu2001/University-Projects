package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CatFoodTest {
    private static final Long catId = 1L;
    private static final Long newCatId = 2L;
    private static final Long foodId = 3L;
    private static final Long newFooId = 4L;

    private static CatFood catFood;

    @Before
    public void setUp() {
        catFood = new CatFood(catId, foodId);
    }

    @After
    public void tearDown() {
        catFood = null;
    }

    @Test
    public void testGetCatId() {
        assertEquals(catId, catFood.getCatId());
    }

    @Test
    public void testSetCatId() {
        catFood.setCatId(newCatId);
        assertEquals(newCatId, catFood.getCatId());
    }

    @Test
    public void testGetFoodId() {
        assertEquals(foodId, catFood.getFoodId());
    }

    @Test
    public void testSetFoodId() {
        catFood.setFoodId(newFooId);
        assertEquals(newFooId, catFood.getFoodId());
    }

    @Test
    public void testEqualForDifferentCatFoodsShouldReturnFalse(){
        CatFood newCatFood = new CatFood(newCatId, newFooId);
        assertNotEquals(catFood, newCatFood);
    }

    @Test
    public void testEqualForSameCatFoodsShouldReturnTrue(){
        CatFood newCatFood = new CatFood(catId, foodId);
        assertEquals(catFood, newCatFood);
    }

    @Test
    public void testEqualForDifferentClassShouldReturnFalse(){
        assertNotEquals(catFood, new BaseEntity<>());
    }

}
