package service;

import domain.Cat;
import domain.CatFood;
import domain.exceptions.PetShopException;
import domain.exceptions.ValidatorException;
import domain.Food;
import domain.Pair;
import repository.IRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class Service {
    IRepository<Long, Cat> catsRepository;
    IRepository<Long, Food> foodRepository;
    IRepository<Pair<Long, Long>, CatFood> catFoodRepository;


    public Service(IRepository<Long, Cat> catsRepository, IRepository<Long, Food> foodRepository,
                   IRepository<Pair<Long, Long>, CatFood> catFoodRepository) {
        this.catsRepository = catsRepository;
        this.foodRepository = foodRepository;
        this.catFoodRepository = catFoodRepository;
    }

    /**
     * Saves the cat with the given attributes to the repository of cats.
     * @param id must not be null
     * @throws IllegalArgumentException
     *              if the given id is null.
     * @throws ValidatorException
     *              if the cat entity is not valid.
     */

    public void addCat(Long id, String name, String owner, Integer catYears) {
        Cat catToBeAdded = new Cat(id, name, owner, catYears);
        catsRepository.save(catToBeAdded);
    }

    /**
     * Saves the food with the given attributes to the food repository.
     * @param id must not be null
     * @throws IllegalArgumentException
     *              if the given id is null.
     * @throws ValidatorException
     *              if the food entity is not valid.
     */
    public void addFood(Long id, String name, String producer, Date expirationDate){
        Food foodToBeAdded = new Food(id, name, producer, expirationDate);
        foodRepository.save(foodToBeAdded);
    }

    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     * @param catId must not be null
     * @param foodId must not be null
     * @throws PetShopException
     *             if catId or foodId does not exist
     */
    public void addCatFood(Long catId, Long foodId){
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
     *
     * @return all cats from the repository.
     */
    public Set<Cat> getCatsFromRepository(){
        return (Set<Cat>) catsRepository.findAll();
    }

    /**
     *
     * @return all the food from the repository.
     */
    public Set<Food> getFoodFromRepository(){
        return (Set<Food>) foodRepository.findAll();
    }

    /**
     *
     * @return the cats with the food they eat from the repository
     */
    public Set<CatFood> getCatFoodFromRepository(){
        return (Set<CatFood>) catFoodRepository.findAll();
    }

}
