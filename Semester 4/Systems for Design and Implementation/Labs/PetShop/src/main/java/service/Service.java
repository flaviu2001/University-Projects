package service;

import domain.Cat;
import domain.CatFood;
import domain.exceptions.PetShopException;
import domain.exceptions.ValidatorException;
import domain.Food;
import domain.Pair;
import repository.IRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {
    final IRepository<Long, Cat> catsRepository;
    final IRepository<Long, Food> foodRepository;
    final IRepository<Pair<Long, Long>, CatFood> catFoodRepository;


    public Service(IRepository<Long, Cat> catsRepository, IRepository<Long, Food> foodRepository,
                   IRepository<Pair<Long, Long>, CatFood> catFoodRepository) {
        this.catsRepository = catsRepository;
        this.foodRepository = foodRepository;
        this.catFoodRepository = catFoodRepository;
    }

    /**
     * Saves the cat with the given attributes to the repository of cats.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    public void addCat(Long id, String name, String owner, Integer catYears) {
        Cat catToBeAdded = new Cat(id, name, owner, catYears);
        catsRepository.save(catToBeAdded);
    }

    /**
     * Saves the food with the given attributes to the food repository.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the food entity is not valid.
     */
    public void addFood(Long id, String name, String producer, Date expirationDate) {
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
     * Updates the cat with the given attributes.
     *
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */

    public void updateCat(Long id, String name, String owner, Integer catYears) {
        catsRepository.update(new Cat(id, name, owner, catYears))
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
}
