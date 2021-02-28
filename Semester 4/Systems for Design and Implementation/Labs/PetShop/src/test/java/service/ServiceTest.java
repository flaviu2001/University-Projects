package service;

import domain.Cat;
import domain.CatFood;
import domain.exceptions.PetShopException;
import domain.exceptions.ValidatorException;
import domain.Food;
import domain.Pair;
import domain.validators.CatFoodValidator;
import domain.validators.CatValidator;
import domain.validators.FoodValidator;
import repository.IRepository;
import repository.InMemoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class ServiceTest {
    private static final IRepository<Long, Cat> catsRepository = new InMemoryRepository<>(new CatValidator());
    private static final IRepository<Long, Food> foodRepository = new InMemoryRepository<>(new FoodValidator());
    private static final IRepository<Pair<Long, Long>, CatFood> catFoodRepository =
            new InMemoryRepository<>(new CatFoodValidator());
    private Service service = new Service(catsRepository, foodRepository, catFoodRepository);

    private static final Long catId = 1L;
    private static final String catName = "cat1";
    private static final String catOwner = "o1";
    private static final Integer catAge = 5;
    private static final Long newCatId = 2L;
    private static final String newCatName = "cat2";
    private static final String newCatOwner = "o2";
    private static final Integer newCatAge = 10;

    private static final Long foodId = 2L;
    private static final String foodName = "n1";
    private static final String foodProducer = "p1";
    private static Date foodExpirationDate;
    private static final Long newFoodId = 5L;
    private static final String newFoodName = "cat2";
    private static final String newFoodProducer = "o2";

    @Before
    public void setUp(){
        service = new Service(catsRepository, foodRepository, catFoodRepository);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 1);
        foodExpirationDate = calendar.getTime();
    }

    @After
    public void tearDown(){
        service = null;
    }

    @Test
    @Order(1)
    public void testAddCatValid(){
        service.addCat(catId, catName, catOwner, catAge);
        assertEquals(service.getCatsFromRepository().size(), 1);
    }

    @Test
    public void testEmptyNameAddCat(){
        Throwable exception = Assertions.assertThrows(ValidatorException.class,
                () -> service.addCat(catId, "", catOwner, catAge));
        assertEquals("Cat name must not be empty", exception.getMessage());
    }

    @Test
    public void testValidAddFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        assertEquals(service.getFoodFromRepository().size(), 1);
    }

    @Test
    public void testEmptyNameAddFood(){
        Throwable exception = Assertions.assertThrows(ValidatorException.class,
                () -> service.addFood(foodId, "", foodProducer, foodExpirationDate));
        assertEquals("Food name must not be empty", exception.getMessage());
    }

    @Test
    public void testGetCatsFromRepository(){
        //service.addCat(catId, catName, catOwner, catAge);
        service.addCat(newCatId, newCatName, newCatOwner, newCatAge);
        assertEquals(service.getCatsFromRepository().size(), 2);
    }

    @Test
    public void testGetFoodFromRepository(){
        //service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);
        assertEquals(service.getFoodFromRepository().size(), 2);
    }

    @Test
    public void testValidAddCatFood(){
        service.addCatFood(catId, foodId);
        assertEquals(service.getCatFoodFromRepository().size(), 1);
    }
    @Test
    public void testInvalidCatIdAddCatFood(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.addCatFood(5L, foodId));
        assertEquals("Cat id does not exist!", exception.getMessage());
    }

    @Test
    public void testInvalidFoodIdAddCatFood(){
        service.addCat(catId, catName, catOwner, catAge);
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.addCatFood(catId, 7L));
        assertEquals("Food id does not exist!", exception.getMessage());
    }
}
