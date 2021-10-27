package domain;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CatFoodTest {
    private static final Long catId = 1L;
    private static final Long newCatId = 2L;
    private static final Long foodId = 3L;
    private static final Long newFooId = 4L;

    private static CatFood catFood;

    @BeforeEach
    public void setUp() {
        catFood = new CatFood(catId, foodId);
    }

    @AfterEach
    public void tearDown() {
        catFood = null;
    }

    @Test
    public void testGetCatId() {
        Assertions.assertEquals(catId, catFood.getCatId());
    }

    @Test
    public void testSetCatId() {
        catFood.setCatId(newCatId);
        Assertions.assertEquals(newCatId, catFood.getCatId());
    }

    @Test
    public void testGetFoodId() {
        Assertions.assertEquals(foodId, catFood.getFoodId());
    }

    @Test
    public void testSetFoodId() {
        catFood.setFoodId(newFooId);
        Assertions.assertEquals(newFooId, catFood.getFoodId());
    }

    @Test
    public void testEqualForDifferentCatFoodsShouldReturnFalse(){
        CatFood newCatFood = new CatFood(newCatId, newFooId);
        Assertions.assertNotEquals(catFood, newCatFood);
    }

    @Test
    public void testEqualForSameCatFoodsShouldReturnTrue(){
        CatFood newCatFood = new CatFood(catId, foodId);
        Assertions.assertEquals(catFood, newCatFood);
    }

    @Test
    public void testEqualForDifferentClassShouldReturnFalse(){
        //noinspection AssertBetweenInconvertibleTypes
        Assertions.assertNotEquals(catFood, new BaseEntity<>());
    }

}
