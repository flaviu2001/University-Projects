package service;

import domain.Cat;
import domain.CatFood;
import domain.Food;
import domain.Pair;
import domain.validators.CatFoodValidator;
import domain.validators.CatValidator;
import domain.validators.FoodValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.InMemoryRepository;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServiceFilterJoinTest {
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    private Service service;

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


    @BeforeEach
    public void setUp(){
        catsRepository = new InMemoryRepository<>(new CatValidator());
        foodRepository = new InMemoryRepository<>(new FoodValidator());
        catFoodRepository = new InMemoryRepository<>(new CatFoodValidator());
        service = new Service(catsRepository, foodRepository, catFoodRepository);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 1);
        foodExpirationDate = calendar.getTime();
    }

    @AfterEach
    public void tearDown(){
        catFoodRepository = null;
        catsRepository = null;
        foodRepository = null;
        service = null;
    }

    @Test
    public void testJoin(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addCat(catId, catName, catOwner, catAge);
        service.addCatFood(catId, foodId);

        List<Pair<Cat, Food>> join = service.getCatFoodJoin();

        assertEquals(join.size(), 1);
        assertEquals(join.get(0).getLeft().getId(), catId);
        assertEquals(join.get(0).getRight().getId(), foodId);
    }

    @Test
    public void testFilter(){
        service.addCat(catId, catName, catOwner, catAge);
        service.addCat(newCatId, newCatName, newCatOwner, newCatAge);

        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);

        service.addCatFood(catId, foodId);
        service.addCatFood(newCatId, newFoodId);

        List<Cat> cats = service.filterCatsThatEatCertainFood(foodId);

        assertEquals(cats.size(), 1);
        assertEquals(cats.get(0).getId(), catId);
    }
}
