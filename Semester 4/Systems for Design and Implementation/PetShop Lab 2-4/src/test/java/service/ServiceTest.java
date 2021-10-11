package service;

import domain.*;
import domain.exceptions.PetShopException;
import domain.exceptions.ValidatorException;
import domain.validators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.Calendar;
import java.util.Date;

public class ServiceTest {
    private static final IRepository<Long, Cat> catsRepository = new InMemoryRepository<>(new CatValidator());
    private static final IRepository<Long, Food> foodRepository = new InMemoryRepository<>(new FoodValidator());
    private static final IRepository<Pair<Long, Long>, CatFood> catFoodRepository =
            new InMemoryRepository<>(new CatFoodValidator());
    private static final IRepository<Long, Customer> customerRepository= new InMemoryRepository<>(new CustomerValidator());
    private static final IRepository<Pair<Long, Long>, Purchase> purchaseRepository = new InMemoryRepository<>(new PurchaseValidator());
    private Service service = new Service(catsRepository, foodRepository, catFoodRepository, customerRepository, purchaseRepository);

    private static final Long catId = 1L;
    private static final String catName = "cat1";
    private static final String catBreed = "o1";
    private static final Integer catAge = 5;
    private static final Long newCatId = 2L;
    private static final String newCatName = "cat2";
    private static final String newCatBreed = "o2";
    private static final Integer newCatAge = 10;

    private static final Long foodId = 2L;
    private static final String foodName = "n1";
    private static final String foodProducer = "p1";
    private static Date foodExpirationDate;
    private static final Long newFoodId = 5L;
    private static final String newFoodName = "cat2";
    private static final String newFoodProducer = "o2";

    private static final Long customerId = 3L;
    private static final String customerName = "Tavi";
    private static final String customerPhoneNumber = "0723456789";
    private static final String invalidCustomerPhoneNumber = "072345678";

    private static final int purchasePrice = 24;
    private static final int purchaseReview = 5;
    private static Date purchaseDate;
    private static Date invalidDate;

    @BeforeEach
    public void setUp(){
        service = new Service(catsRepository, foodRepository, catFoodRepository, customerRepository, purchaseRepository);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 1);
        foodExpirationDate = calendar.getTime();
        purchaseDate = calendar.getTime();
        calendar.set(2060, Calendar.DECEMBER, 31);
        invalidDate = calendar.getTime();
    }

    @AfterEach
    public void tearDown(){
        service = null;
    }

    @Test
    public void testAddCatValid(){
        service.addCat(catId, catName, catBreed, catAge);
        Assertions.assertEquals(service.getCatsFromRepository().size(), 1);
    }

    @Test
    public void testEmptyNameAddCat(){
        Assertions.assertThrows(ValidatorException.class, () -> service.addCat(catId, "", catBreed, catAge));
    }

    @Test
    public void testValidAddFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        Assertions.assertEquals(service.getFoodFromRepository().size(), 1);
    }

    @Test
    public void testEmptyNameAddFood(){
        Throwable exception = Assertions.assertThrows(ValidatorException.class,
                () -> service.addFood(foodId, "", foodProducer, foodExpirationDate));
        Assertions.assertEquals("Food name must not be empty", exception.getMessage());
    }

    @Test
    public void testGetCatsFromRepository(){
        //service.addCat(catId, catName, catOwner, catAge);
        service.addCat(newCatId, newCatName, newCatBreed, newCatAge);
        Assertions.assertEquals(service.getCatsFromRepository().size(), 2);
    }

    @Test
    public void testGetFoodFromRepository(){
        //service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);
        Assertions.assertEquals(service.getFoodFromRepository().size(), 2);
    }

    @Test
    public void testValidAddCatFood(){
        service.addCatFood(catId, foodId);
        Assertions.assertEquals(service.getCatFoodFromRepository().size(), 1);
    }
    @Test
    public void testInvalidCatIdAddCatFood(){
        Assertions.assertThrows(PetShopException.class, () -> service.addCatFood(5L, foodId));
    }

    @Test
    public void testInvalidFoodIdAddCatFood(){
        service.addCat(catId, catName, catBreed, catAge);
        Assertions.assertThrows(PetShopException.class, () -> service.addCatFood(catId, 7L));
    }

    @Test
    public void testValidAddCustomer() {
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        Assertions.assertEquals(service.getCustomersFromRepository().size(), 1);
    }

    @Test
    public void testInvalidPhoneNumberAddCustomer(){
        Assertions.assertThrows(PetShopException.class, () -> service.addCustomer(customerId, customerName, invalidCustomerPhoneNumber));
    }


    @Test
    public void testValidAddPurchase() {
        service.addPurchase(catId, customerId, purchasePrice, purchaseDate, purchaseReview);
        Assertions.assertEquals(service.getPurchasesFromRepository().size(), 1);
    }

    @Test
    public void testInvalidCatIdAddPurchase() {
        Assertions.assertThrows(PetShopException.class, () -> service.addPurchase(11L, customerId, purchasePrice, purchaseDate, purchaseReview));
    }

    @Test
    public void testInvalidCustomerIdAddPurchase() {
        Assertions.assertThrows(PetShopException.class, () -> service.addPurchase(catId, 11L, purchasePrice, purchaseDate, purchaseReview));
    }
    @Test
    public void testInvalidDateAddPurchase() {
        service.addCat(catId, "n1", "b1", 4);
        service.addCustomer(customerId, "n2", "0123456789");

        Assertions.assertThrows(PetShopException.class, () -> service.addPurchase(catId, customerId, purchasePrice, invalidDate, purchaseReview));
    }
    @Test
    public void testInvalidReviewAddPurchase() {
        Assertions.assertThrows(PetShopException.class, () -> service.addPurchase(catId, customerId, purchasePrice, purchaseDate, 7));
    }
}
