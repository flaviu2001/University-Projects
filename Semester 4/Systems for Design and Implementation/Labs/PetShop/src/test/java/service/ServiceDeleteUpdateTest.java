package service;

import domain.Cat;
import domain.CatFood;
import domain.Food;
import domain.Pair;
import domain.exceptions.PetShopException;
import domain.validators.CatFoodValidator;
import domain.validators.CatValidator;
import domain.validators.FoodValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServiceDeleteUpdateTest {
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    private Service service;

    private static final Long catId = 1L;
    private static final String catName = "cat1";
    private static final String catOwner = "o1";
    private static final Integer catAge = 5;
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

    private static final Long deletedCatId = 11L;
    private static final Long deletedFoodId = 11L;

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
    public void testDeleteValidCat(){
        service.addCat(deletedCatId, catName, catOwner, catAge);
        int originalSize = service.getCatsFromRepository().size();
        service.deleteCat(deletedCatId);

        assertEquals(service.getCatsFromRepository().size(), originalSize-1);
    }

    @Test
    public void testDeleteNonexistentCat(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.deleteCat(deletedCatId));
        assertEquals("Cat does not exist", exception.getMessage());
    }

    @Test
    public void testDeleteCatThatIsFed(){
        service.addCat(deletedCatId, catName, catOwner, catAge);
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        service.addCatFood(deletedCatId, deletedFoodId);

        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.deleteCat(deletedFoodId));
        assertEquals("Cat is currently fed", exception.getMessage());
    }

    @Test
    public void testDeleteValidFood(){
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        int originalSize = service.getFoodFromRepository().size();
        service.deleteFood(deletedFoodId);

        assertEquals(service.getFoodFromRepository().size(), originalSize-1);
    }

    @Test
    public void testDeleteNonexistentFood(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.deleteFood(deletedFoodId));
        assertEquals("Food does not exist", exception.getMessage());
    }

    @Test
    public void testDeleteFoodThatIsEaten(){
        service.addCat(deletedCatId, catName, catOwner, catAge);
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        service.addCatFood(deletedCatId, deletedFoodId);

        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.deleteFood(deletedFoodId));
        assertEquals("Food is currently eaten", exception.getMessage());
    }

    @Test
    public void testDeleteValidCatFood(){
        service.addCat(deletedCatId, catName, catOwner, catAge);
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        service.addCatFood(deletedCatId, deletedFoodId);
        int originalSize = service.getCatFoodFromRepository().size();

        service.deleteCatFood(deletedCatId, deletedFoodId);

        assertEquals(service.getCatFoodFromRepository().size(), originalSize-1);
    }

    @Test
    public void testDeleteInvalidCatFood(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.deleteCatFood(deletedCatId, 7L));
        assertEquals("Cat food does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateValidCat(){
        service.addCat(catId, catName, catOwner, catAge);
        service.updateCat(catId, newCatName, newCatOwner, newCatAge);
        Optional<Cat> catOptional = service.catsRepository.findOne(catId);
        assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(catId) &&
                catOptional.get().getName().equals(newCatName) &&
                catOptional.get().getOwner().equals(newCatOwner) &&
                catOptional.get().getCatYears().equals(newCatAge) &&
                service.getCatsFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateNonexistentCat(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.updateCat(catId, catName, catOwner, catAge));
        assertEquals("Cat does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateValidFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.updateFood(foodId, newFoodName, newFoodProducer, foodExpirationDate);
        Optional<Food> foodOptional = service.foodRepository.findOne(foodId);
        assertTrue(foodOptional.isPresent() &&
                foodOptional.get().getId().equals(foodId) &&
                foodOptional.get().getName().equals(newFoodName) &&
                foodOptional.get().getProducer().equals(newFoodProducer) &&
                foodOptional.get().getExpirationDate().equals(foodExpirationDate) &&
                service.getFoodFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateNonexistentFood(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.updateFood(foodId, foodName, foodProducer, foodExpirationDate));
        assertEquals("Food does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateValidCatFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);
        service.addCat(catId, catName, catOwner, catAge);

        service.addCatFood(catId, foodId);
        service.updateCatFood(catId, foodId, newFoodId);

        Optional<CatFood> catFoodOptional = service.catFoodRepository.findOne(new Pair<>(catId, newFoodId));
        assertTrue(catFoodOptional.isPresent() &&
                service.getCatFoodFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateNonexistentCatFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addCat(catId, catName, catOwner, catAge);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);

        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.updateCatFood(catId, foodId, newFoodId));
        assertEquals("Cat food does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateCatFoodNonExistentCat(){
        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.updateCatFood(catId, foodId, newFoodId));
        assertEquals("Cat does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateCatFoodNonExistentFood(){
        service.addCat(catId, catName, catOwner, catAge);

        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.updateCatFood(catId, foodId, newFoodId));
        assertEquals("Food does not exist", exception.getMessage());
    }

    @Test
    public void testUpdateCatFoodNonExistentNewFood(){
        service.addCat(catId, catName, catOwner, catAge);
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);

        Throwable exception = Assertions.assertThrows(PetShopException.class,
                () -> service.updateCatFood(catId, foodId, newFoodId));
        assertEquals("New food does not exist", exception.getMessage());
    }
}
