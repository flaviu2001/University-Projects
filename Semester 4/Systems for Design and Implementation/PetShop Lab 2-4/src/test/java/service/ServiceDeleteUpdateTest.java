package service;

import domain.*;
import domain.exceptions.PetShopException;
import domain.validators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class ServiceDeleteUpdateTest {
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    private IRepository<Long, Customer> customerRepository;
    private IRepository<Pair<Long, Long>, Purchase> purchaseRepository;
    private Service service;

    private static final Long catId = 1L;
    private static final String catName = "cat1";
    private static final String catBreed = "o1";
    private static final Integer catAge = 5;
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

    private static final Long deletedCatId = 11L;
    private static final Long deletedFoodId = 11L;

    private static final Long customerId = 3L;
    private static final String customerName = "Tavi";
    private static final String newCustomerName = "Ana";
    private static final String customerPhoneNumber = "0723456789";
    private static final String invalidCustomerPhoneNumber = "072345678";

    private static final int purchasePrice = 24;
    private static final int newPurchaseReview = 3;
    private static final int purchaseReview = 5;

    private static final int invalidPurchaseReview = 7;

    private static Date purchaseDate;

    @BeforeEach
    public void setUp(){
        catsRepository = new InMemoryRepository<>(new CatValidator());
        foodRepository = new InMemoryRepository<>(new FoodValidator());
        catFoodRepository = new InMemoryRepository<>(new CatFoodValidator());
        customerRepository = new InMemoryRepository<>(new CustomerValidator());
        purchaseRepository = new InMemoryRepository<>(new PurchaseValidator());
        service = new Service(catsRepository, foodRepository, catFoodRepository, customerRepository, purchaseRepository);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.FEBRUARY, 1);
        foodExpirationDate = calendar.getTime();
        purchaseDate = calendar.getTime();
    }

    @AfterEach
    public void tearDown(){
        catFoodRepository = null;
        catsRepository = null;
        foodRepository = null;
        customerRepository = null;
        purchaseRepository = null;
        service = null;
    }

    @Test
    public void testDeleteValidCat(){
        service.addCat(deletedCatId, catName, catBreed, catAge);
        int originalSize = service.getCatsFromRepository().size();
        service.deleteCat(deletedCatId);

        Assertions.assertEquals(service.getCatsFromRepository().size(), originalSize-1);
    }

    @Test
    public void testDeleteNonexistentCat(){
        Assertions.assertThrows(PetShopException.class, () -> service.deleteCat(deletedCatId));
    }

    @Test
    public void testDeleteCatThatIsFed(){
        service.addCat(deletedCatId, catName, catBreed, catAge);
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        service.addCatFood(deletedCatId, deletedFoodId);

        Assertions.assertThrows(PetShopException.class, () -> service.deleteCat(deletedFoodId));
    }

    @Test
    public void testDeleteValidFood(){
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        int originalSize = service.getFoodFromRepository().size();
        service.deleteFood(deletedFoodId);

        Assertions.assertEquals(service.getFoodFromRepository().size(), originalSize-1);
    }

    @Test
    public void testDeleteNonexistentFood(){
        Assertions.assertThrows(PetShopException.class, () -> service.deleteFood(deletedFoodId));
    }

    @Test
    public void testDeleteFoodThatIsEaten(){
        service.addCat(deletedCatId, catName, catBreed, catAge);
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        service.addCatFood(deletedCatId, deletedFoodId);

        Assertions.assertThrows(PetShopException.class, () -> service.deleteFood(deletedFoodId));
    }

    @Test
    public void testDeleteValidCatFood(){
        service.addCat(deletedCatId, catName, catBreed, catAge);
        service.addFood(deletedFoodId, foodName, foodProducer, foodExpirationDate);
        service.addCatFood(deletedCatId, deletedFoodId);
        int originalSize = service.getCatFoodFromRepository().size();

        service.deleteCatFood(deletedCatId, deletedFoodId);

        Assertions.assertEquals(service.getCatFoodFromRepository().size(), originalSize-1);
    }

    @Test
    public void testDeleteInvalidCatFood(){
        Assertions.assertThrows(PetShopException.class, () -> service.deleteCatFood(deletedCatId, 7L));
    }

    @Test
    public void testUpdateValidCat(){
        service.addCat(catId, catName, catBreed, catAge);
        service.updateCat(catId, newCatName, newCatBreed, newCatAge);
        Optional<Cat> catOptional = service.catsRepository.findOne(catId);
        Assertions.assertTrue(catOptional.isPresent() &&
                catOptional.get().getId().equals(catId) &&
                catOptional.get().getName().equals(newCatName) &&
                catOptional.get().getBreed().equals(newCatBreed) &&
                catOptional.get().getCatYears().equals(newCatAge) &&
                service.getCatsFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateNonexistentCat(){
        Assertions.assertThrows(PetShopException.class, () -> service.updateCat(catId, catName, catBreed, catAge));
    }

    @Test
    public void testDeleteValidCustomer(){
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        service.deleteCustomer(customerId);
        Assertions.assertEquals(service.getCustomersFromRepository().size(), 0);
    }

    @Test
    public void testDeleteNonExistentCustomer(){
        Assertions.assertThrows(PetShopException.class, () -> service.deleteCustomer(customerId));
    }

    @Test
    public void testDeleteValidPurchase() {
        service.addCat(catId, catName, catBreed, catAge);
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        service.addPurchase(catId, customerId, purchasePrice, purchaseDate, purchaseReview);
        service.deletePurchase(catId, customerId);
        Assertions.assertEquals(service.getPurchasesFromRepository().size(), 0);
    }

    @Test
    public void testDeleteNonExistentPurchase() {
        Assertions.assertThrows(PetShopException.class, () -> service.deletePurchase(catId, customerId));
    }

    @Test
    public void testUpdateValidFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.updateFood(foodId, newFoodName, newFoodProducer, foodExpirationDate);
        Optional<Food> foodOptional = service.foodRepository.findOne(foodId);
        Assertions.assertTrue(foodOptional.isPresent() &&
                foodOptional.get().getId().equals(foodId) &&
                foodOptional.get().getName().equals(newFoodName) &&
                foodOptional.get().getProducer().equals(newFoodProducer) &&
                foodOptional.get().getExpirationDate().equals(foodExpirationDate) &&
                service.getFoodFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateNonexistentFood(){
        Assertions.assertThrows(PetShopException.class, () -> service.updateFood(foodId, foodName, foodProducer, foodExpirationDate));
    }

    @Test
    public void testUpdateValidCatFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);
        service.addCat(catId, catName, catBreed, catAge);

        service.addCatFood(catId, foodId);
        service.updateCatFood(catId, foodId, newFoodId);

        Optional<CatFood> catFoodOptional = service.catFoodRepository.findOne(new Pair<>(catId, newFoodId));
        Assertions.assertTrue(catFoodOptional.isPresent() &&
                service.getCatFoodFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateNonexistentCatFood(){
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);
        service.addCat(catId, catName, catBreed, catAge);
        service.addFood(newFoodId, newFoodName, newFoodProducer, foodExpirationDate);

        Assertions.assertThrows(PetShopException.class, () -> service.updateCatFood(catId, foodId, newFoodId));
    }

    @Test
    public void testUpdateCatFoodNonExistentCat(){
        Assertions.assertThrows(PetShopException.class, () -> service.updateCatFood(catId, foodId, newFoodId));
    }

    @Test
    public void testUpdateCatFoodNonExistentFood(){
        service.addCat(catId, catName, catBreed, catAge);

        Assertions.assertThrows(PetShopException.class, () -> service.updateCatFood(catId, foodId, newFoodId));
    }

    @Test
    public void testUpdateCatFoodNonExistentNewFood(){
        service.addCat(catId, catName, catBreed, catAge);
        service.addFood(foodId, foodName, foodProducer, foodExpirationDate);

        Assertions.assertThrows(PetShopException.class, () -> service.updateCatFood(catId, foodId, newFoodId));
    }

    @Test
    public void testUpdateValidCustomer(){
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        service.updateCustomer(customerId, newCustomerName, customerPhoneNumber);
        Optional<Customer> customerOptional = service.customerRepository.findOne(customerId);
        Assertions.assertTrue(customerOptional.isPresent() &&
                customerOptional.get().getId().equals(customerId) &&
                customerOptional.get().getName().equals(newCustomerName) &&
                customerOptional.get().getPhoneNumber().equals(customerPhoneNumber) &&
                service.getCustomersFromRepository().size() == 1
        );
    }

    @Test
    public void testUpdateValidPurchase() {
        service.addCat(catId, catName, catBreed, catAge);
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        service.addPurchase(catId, customerId, purchasePrice, purchaseDate, purchaseReview);

        service.updatePurchase(catId, customerId, newPurchaseReview);
        Optional<Purchase> purchaseOptional = service.purchaseRepository.findOne(new Pair<>(catId, customerId));
        Assertions.assertTrue(purchaseOptional.isPresent() && service.getPurchasesFromRepository().size() == 1);
        Assertions.assertEquals(purchaseOptional.get().getReview(), newPurchaseReview);
    }

    @Test
    public void testUpdateNonExistentCustomer(){
        Assertions.assertThrows(PetShopException.class, () -> service.updateCustomer(customerId, customerName, customerPhoneNumber));
    }

    @Test
    public void testUpdateNonExistentPurchase() {
        Assertions.assertThrows(PetShopException.class, () -> service.updatePurchase(catId, customerId, purchaseReview));
    }


    @Test
    public void testUpdatePurchaseInvalidReview() {
        service.addCat(catId, catName, catBreed, catAge);
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        service.addPurchase(catId, customerId, purchasePrice, purchaseDate, purchaseReview);
        Assertions.assertThrows(PetShopException.class, () -> service.updatePurchase(catId, customerId, invalidPurchaseReview));
    }

    @Test
    public void testUpdateCustomerInvalidPhoneNumber(){
        service.addCustomer(customerId, customerName, customerPhoneNumber);
        Assertions.assertThrows(PetShopException.class, () -> service.updateCustomer(customerId, customerName, invalidCustomerPhoneNumber));
    }
}
