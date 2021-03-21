package service;

import common.domain.*;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.Math.max;

public class Service {
    final IRepository<Long, Cat> catsRepository;
    final IRepository<Long, Food> foodRepository;
    final IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    final IRepository<Long, Customer> customerRepository;
    final IRepository<Pair<Long, Long>, Purchase> purchaseRepository;

    public Service(IRepository<Long, Cat> catsRepository, IRepository<Long, Food> foodRepository,
                   IRepository<Pair<Long, Long>, CatFood> catFoodRepository,
                   IRepository<Long, Customer> customerRepository,
                   IRepository<Pair<Long, Long>, Purchase> purchaseRepository) {
        this.catsRepository = catsRepository;
        this.foodRepository = foodRepository;
        this.catFoodRepository = catFoodRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
    }

    /**
     * Saves the cat with the given attributes to the repository of cats.
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    public void addCat(String name, String breed, Integer catYears) {
        long id = 0;
        for (Cat cat :this.catsRepository.findAll())
            id = max(id, cat.getId()+1);
        Cat catToBeAdded = new Cat(id, name, breed, catYears);
        catsRepository.save(catToBeAdded);
    }

    /**
     * Saves the food with the given attributes to the food repository.
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the food entity is not valid.
     */
    public void addFood(String name, String producer, Date expirationDate) {
        long id = 0;
        for (Food food:this.foodRepository.findAll())
            id = max(id, food.getId()+1);
        Food foodToBeAdded = new Food(id, name, producer, expirationDate);
        foodRepository.save(foodToBeAdded);
    }

    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param catId  must not be null
     * @param foodId must not be null
     * @throws PetShopException if catId or foodId does not exist
     */
    public void addCatFood(Long catId, Long foodId) {
        Optional<Cat> cat = catsRepository.findOne(catId);
        cat.ifPresentOrElse((Cat c) -> {
            Optional<Food> food = foodRepository.findOne(foodId);
            food.ifPresentOrElse((Food f) -> {
                CatFood catFood = new CatFood(catId, foodId);
                catFoodRepository.save(catFood);
            }, () -> {
                throw new PetShopException("Food id does not exist!");
            });
        }, () -> {
            throw new PetShopException("Cat id does not exist!");
        });
    }

    /**
     * Saves the customer with the given attributes to the repository of customers
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */
    public void addCustomer(String name, String phoneNumber) {
        long id = 0;
        for (Customer customer:this.customerRepository.findAll())
            id = max(id, customer.getId()+1);
        Customer customerToBeAdded = new Customer(id, name, phoneNumber);
        customerRepository.save(customerToBeAdded);
    }

    /**
     * Saves the purchase with the given attributes to the repository of purchases
     *
     * @param catId        must not be null
     * @param customerId   must not be null
     * @param price        price of the purchase
     * @param dateAcquired date when it was made
     * @param review       number of stars given by the customer
     * @throws PetShopException if catId or customerId does not exist
     */
    public void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        List<Long> catIds = new ArrayList<>();
        purchaseRepository.findAll().forEach(purchase -> catIds.add(purchase.getCatId()));
        catIds.stream().filter(cat -> cat.equals(catId)).findAny().ifPresent(cat -> {
            throw new PetShopException("The cat is already purchased");
        });
        Optional<Cat> cat = catsRepository.findOne(catId);
        cat.ifPresentOrElse((Cat c) -> {
            Optional<Customer> customer = customerRepository.findOne(customerId);
            customer.ifPresentOrElse((Customer cust) -> {
                Purchase purchase = new Purchase(catId, customerId, price, dateAcquired, review);
                purchaseRepository.save(purchase);
            }, () -> {
                throw new PetShopException("Customer id does not exist");
            });
        }, () -> {
            throw new PetShopException("Cat id does not exist");
        });
    }

    /**
     * @return all cats from the repository.
     */
    public Set<Cat> getCatsFromRepository() {
        return (Set<Cat>) catsRepository.findAll();
    }

    /**
     * @return all the food from the repository.
     */
    public Set<Food> getFoodFromRepository() {
        return (Set<Food>) foodRepository.findAll();
    }

    /**
     * @return the cats with the food they eat from the repository
     */
    public Set<CatFood> getCatFoodFromRepository() {
        return (Set<CatFood>) catFoodRepository.findAll();
    }

    /**
     * @return all customers from the repository
     */
    public Set<Customer> getCustomersFromRepository() {
        return (Set<Customer>) customerRepository.findAll();
    }

    /**
     * @return all purchases from the repository
     */
    public Set<Purchase> getPurchasesFromRepository() {
        return (Set<Purchase>) purchaseRepository.findAll();
    }

    /**
     * @return the join between cats and the food they eat
     * @throws PetShopException if there are cat foods for nonexistent cats or foods
     */
    public List<Pair<Cat, Food>> getCatFoodJoin() {
        List<Pair<Cat, Food>> join = new LinkedList<>();
        getCatFoodFromRepository().forEach(catFood -> join.add(new Pair<>(
                catsRepository.findOne(catFood.getCatId()).orElseThrow(() -> new PetShopException("Cat does not exist")),
                foodRepository.findOne(catFood.getFoodId()).orElseThrow(() -> new PetShopException("Food does not exist"))
        )));
        return join;
    }

    /**
     * Deletes a cat based on it's id
     *
     * @param id - id of the cat to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat does not exist
     *                                  if the cat is currently fed
     */
    public void deleteCat(Long id) {
        StreamSupport.stream(catFoodRepository.findAll().spliterator(), false)
                .filter(catFood -> catFood.getCatId().equals(id))
                .findAny()
                .ifPresent((catFood) -> {
                    throw new PetShopException("Cat is currently fed");
                });
        StreamSupport.stream(purchaseRepository.findAll().spliterator(), false)
                .filter(purchase -> purchase.getCatId().equals(id))
                .findAny()
                .ifPresent(purchase -> {
                    throw new PetShopException("The cat is purchased, can't delete");
                });
        catsRepository.delete(id).orElseThrow(() -> new PetShopException("Cat does not exist"));
    }

    /**
     * Deletes a food based on it's id
     *
     * @param id - id of the food to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the food does not exist
     *                                  if the food is currently eaten
     */
    public void deleteFood(Long id) {
        StreamSupport.stream(catFoodRepository.findAll().spliterator(), false)
                .filter(catFood -> catFood.getFoodId().equals(id))
                .findAny()
                .ifPresent((catFood) -> {
                    throw new PetShopException("Food is currently eaten");
                });
        foodRepository.delete(id).orElseThrow(() -> new PetShopException("Food does not exist"));
    }

    /**
     * Deletes the CatFood with the id composed of catId and foodId
     *
     * @param catId  - id of the cat
     * @param foodId - if of the food
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat food does not exist
     */
    public void deleteCatFood(Long catId, Long foodId) {
        catFoodRepository.delete(new Pair<>(catId, foodId))
                .orElseThrow(() -> new PetShopException("Cat food does not exist"));
    }

    /**
     * Deletes a customer based on it's id
     *
     * @param id - id of the customer to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the customer does not exist
     */
    public void deleteCustomer(Long id) {
        customerRepository.delete(id).orElseThrow(() -> {
            throw new PetShopException("Customer does not exist");
        });
    }

    /**
     * Deletes a purchase based on id
     * @param catId must not be null
     * @param customerId must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the purchase does not exist
     */
    public void deletePurchase(Long catId, Long customerId) {
        purchaseRepository.delete(new Pair<>(catId, customerId))
                .orElseThrow(() -> new PetShopException("Purchase does not exist"));
    }

    /**
     * Updates the cat with the given attributes.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    public void updateCat(Long id, String name, String breed, Integer catYears) {
        catsRepository.update(new Cat(id, name, breed, catYears))
                .orElseThrow(() -> new PetShopException("Cat does not exist"));
    }

    /**
     * Updates the food with the given attributes.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the food entity is not valid.
     */
    public void updateFood(Long id, String name, String producer, Date expirationDate) {
        foodRepository.update(new Food(id, name, producer, expirationDate))
                .orElseThrow(() -> new PetShopException("Food does not exist"));
    }

    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param catId  must not be null
     * @param foodId must not be null
     * @throws PetShopException if:
     *                          catId or foodId does not exist
     *                          old Cat food does not exist
     *                          new food does not exist
     */
    public void updateCatFood(Long catId, Long foodId, Long newFoodId) {
        catsRepository.findOne(catId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        foodRepository.findOne(foodId).orElseThrow(() -> new PetShopException("Food does not exist"));
        foodRepository.findOne(newFoodId).orElseThrow(() -> new PetShopException("New food does not exist"));
        catFoodRepository.delete(new Pair<>(catId, foodId))
                .orElseThrow(() -> new PetShopException("Cat food does not exist"));
        addCatFood(catId, newFoodId);
    }

    /**
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the customer entity is not valid.
     * @throws PetShopException         if the customer does not exist
     */
    public void updateCustomer(Long id, String name, String phoneNumber) {
        customerRepository.update(new Customer(id, name, phoneNumber))
                .orElseThrow(() -> new PetShopException("Customer does not exist"));
    }

    /**
     *
     * @param catId must not be null
     * @param customerId must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the purchase entity is not valid.
     * @throws PetShopException         if the purchase does not exist / cat does not exist / customer does not exist
     */
    public void updatePurchase(Long catId, Long customerId, int newReview) {
        catsRepository.findOne(catId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        customerRepository.findOne(customerId).orElseThrow(() -> new PetShopException("Customer does not exist"));
        Purchase purchase = purchaseRepository.findOne(new Pair<>(catId, customerId))
                .orElseThrow(() -> new PetShopException("Purchase does not exist"));
        purchaseRepository.update(new Purchase(catId, customerId, purchase.getPrice(), purchase.getDateAcquired(), newReview));
    }
    /**
     * @param foodId - identifies the required food
     * @return a list of cats that eat the required food
     */
    public List<Cat> filterCatsThatEatCertainFood(Long foodId) {
        return getCatsFromRepository().stream()
                .filter(
                        cat -> getCatFoodFromRepository().stream().anyMatch(
                                catFood -> catFood.getCatId().equals(cat.getId()) &&
                                        catFood.getFoodId().equals(foodId)
                        )
                )
                .collect(Collectors.toList());
    }

    /**
     * @param breed - the breed of cat by which to filter
     * @return a list of all customers who bought at least a cat of a certain breed
     */
    public List<Customer> filterCustomersThatBoughtBreedOfCat(String breed) {
        return getCustomersFromRepository().stream()
            .filter((customer) ->
                getPurchasesFromRepository().stream().anyMatch((purchase) ->
                    getCatsFromRepository().stream().anyMatch((cat) ->
                        purchase.getCatId().equals(cat.getId()) && purchase.getCustomerId().equals(customer.getId()) && cat.getBreed().equals(breed))))
                .collect(Collectors.toList());
    }

    /**
     * @param minStars - the minimum amount of stars by which you filter, must be between 1 and 5
     * @return a list of all purchases with a minimum amount of stars
     */
    public List<Purchase> filterPurchasesWithMinStars(int minStars) {
        return getPurchasesFromRepository().stream()
                .filter(purchase -> purchase.getReview() >= minStars)
                .collect(Collectors.toList());
    }

    /**
     * @return a list of Customer - Integer pairs with spent money from each customer sorted by money spent
     */
    public List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash() {
        List<Pair<Customer, Integer>> toReturn = new ArrayList<>();
        getCustomersFromRepository().forEach((customer) -> {
            int moneySpent = getPurchasesFromRepository().stream()
                    .filter(purchase -> purchase.getCustomerId().equals(customer.getId()))
                    .map(Purchase::getPrice)
                    .reduce(0, Integer::sum);
            toReturn.add(new Pair<>(customer, moneySpent));
        });
        toReturn.sort((p1, p2) -> -p1.getRight().compareTo(p2.getRight()));
        return toReturn;
    }
}
