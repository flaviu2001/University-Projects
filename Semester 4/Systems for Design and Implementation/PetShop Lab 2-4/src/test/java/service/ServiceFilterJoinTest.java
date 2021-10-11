package service;

import domain.*;
import domain.validators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.InMemoryRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ServiceFilterJoinTest {
    private IRepository<Long, Cat> catsRepository;
    private IRepository<Long, Food> foodRepository;
    private IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    private IRepository<Long, Customer> customerRepository;
    private IRepository<Pair<Long, Long>, Purchase> purchaseRepository;
    private Service service;

    private static final Long cat1Id = 1L;
    private static final String cat1Name = "cat1";
    private static final String cat1Breed = "o1";
    private static final Integer cat1Age = 5;
    private static final Long cat2Id = 2L;
    private static final String cat2Name = "cat2";
    private static final String cat2Breed = "o2";
    private static final Integer cat2Age = 10;

    private static final Long food1Id = 2L;
    private static final String food1Name = "n1";
    private static final String food1Producer = "p1";
    private static Date foodExpirationDate;
    private static final Long food2Id = 5L;
    private static final String food2Name = "cat2";
    private static final String food2Producer = "o2";


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
    public void testCatFoodJoin(){
        service.addFood(food1Id, food1Name, food1Producer, foodExpirationDate);
        service.addCat(cat1Id, cat1Name, cat1Breed, cat1Age);
        service.addCatFood(cat1Id, food1Id);

        List<Pair<Cat, Food>> join = service.getCatFoodJoin();

        Assertions.assertEquals(join.size(), 1);
        Assertions.assertEquals(join.get(0).getLeft().getId(), cat1Id);
        Assertions.assertEquals(join.get(0).getRight().getId(), food1Id);
    }

    @Test
    public void testFilterCatsThatEatCertainFood(){
        service.addCat(cat1Id, cat1Name, cat1Breed, cat1Age);
        service.addCat(cat2Id, cat2Name, cat2Breed, cat2Age);

        service.addFood(food1Id, food1Name, food1Producer, foodExpirationDate);
        service.addFood(food2Id, food2Name, food2Producer, foodExpirationDate);

        service.addCatFood(cat1Id, food1Id);
        service.addCatFood(cat2Id, food2Id);

        List<Cat> cats = service.filterCatsThatEatCertainFood(food1Id);

        Assertions.assertEquals(cats.size(), 1);
        Assertions.assertEquals(cats.get(0).getId(), cat1Id);
    }

    public <T extends Comparable<? super T>> boolean equalUnorderedLists(List<T> one, List<T> two){
        one = new ArrayList<>(one);
        two = new ArrayList<>(two);
        Collections.sort(one);
        Collections.sort(two);
        return one.size() == two.size() && one.equals(two);
    }


    @Test
    public void testFilterCustomersThatBoughtBreedOfCat() {
        //Sorry I wanted to use constants like the other tests but it would make the file much larger
        service.addCat(1L, "pisi", "maidaneza", 12);
        service.addCat(2L, "tom", "birmaneza", 21);
        service.addCat(3L, "dom", "maidaneza", 3);
        service.addCat(4L, "brom", "maidaneza", 50);
        service.addCustomer(1L, "John", "0766666669");
        service.addCustomer(2L, "Jack", "0712345678");
        service.addPurchase(1L, 1L, 20, new Date(), 2);
        service.addPurchase(3L, 1L, 25, new Date(), 4);
        service.addPurchase(2L, 2L, 14, new Date(), 5);
        service.addPurchase(4L, 2L, 10, new Date(), 1);
        List<Customer> filter = service.filterCustomersThatBoughtBreedOfCat("birmaneza");
        Assertions.assertTrue(filter.size() == 1 && equalUnorderedLists(Collections.singletonList(filter.get(0).getId()), Collections.singletonList(2L)));
        filter = service.filterCustomersThatBoughtBreedOfCat("maidaneza");
        Assertions.assertTrue(filter.size() == 2 && equalUnorderedLists(Arrays.asList(filter.get(0).getId(), filter.get(1).getId()), Arrays.asList(1L, 2L)));
    }

    @Test
    public void testFilterPurchasesWithMinStars() {
        service.addCat(1L, "pisi", "maidaneza", 12);
        service.addCat(2L, "tom", "birmaneza", 21);
        service.addCat(3L, "dom", "maidaneza", 3);
        service.addCat(4L, "brom", "maidaneza", 50);
        service.addCustomer(1L, "John", "0766666669");
        service.addCustomer(2L, "Jack", "0712345678");
        service.addPurchase(1L, 1L, 20, new Date(), 2);
        service.addPurchase(3L, 1L, 25, new Date(), 4);
        service.addPurchase(2L, 2L, 14, new Date(), 5);
        service.addPurchase(4L, 2L, 10, new Date(), 1);
        List<Purchase> purchases = service.filterPurchasesWithMinStars(3);
        Assertions.assertTrue(equalUnorderedLists(Arrays.asList(
                new ComparablePair<>(3L, 1L),
                new ComparablePair<>(2L, 2L)
                ), purchases.stream()
                        .map(purchase -> new ComparablePair<>(purchase.getCatId(), purchase.getCustomerId()))
                        .collect(Collectors.toList())));
        purchases = service.filterPurchasesWithMinStars(2);
        Assertions.assertTrue(equalUnorderedLists(Arrays.asList(
                new ComparablePair<>(3L, 1L),
                new ComparablePair<>(2L, 2L),
                new ComparablePair<>(1L, 1L)
        ), purchases.stream()
                .map(purchase -> new ComparablePair<>(purchase.getCatId(), purchase.getCustomerId()))
                .collect(Collectors.toList())));
    }

    @Test
    public void testReportCustomersSortedBySpentCash() {
        service.addCat(1L, "pisi", "maidaneza", 12);
        service.addCat(2L, "tom", "birmaneza", 21);
        service.addCat(3L, "dom", "maidaneza", 3);
        service.addCat(4L, "brom", "maidaneza", 50);
        service.addCustomer(1L, "John", "0766666669");
        service.addCustomer(2L, "Jack", "0712345678");
        service.addPurchase(1L, 1L, 20, new Date(), 2);
        service.addPurchase(3L, 1L, 25, new Date(), 4);
        service.addPurchase(2L, 2L, 14, new Date(), 5);
        service.addPurchase(4L, 2L, 10, new Date(), 1);
        List<Pair<Customer, Integer>> customers = service.reportCustomersSortedBySpentCash();
        Assertions.assertEquals(customers, Arrays.asList(
                new Pair<>(service.customerRepository.findOne(1L).orElseThrow(), 45),
                new Pair<>(service.customerRepository.findOne(2L).orElseThrow(), 24)
        ));
    }
}
