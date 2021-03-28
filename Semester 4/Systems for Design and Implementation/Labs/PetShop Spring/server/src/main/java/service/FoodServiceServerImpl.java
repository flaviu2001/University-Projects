package service;

import common.domain.CatFood;
import common.domain.Food;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.service.IFoodService;
import repository.IRepository;

import java.util.Date;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.lang.Math.max;

public class FoodServiceServerImpl implements IFoodService {
    final IRepository<Long, Food> foodRepository;
    final IRepository<Pair<Long, Long>, CatFood> catFoodRepository;

    public FoodServiceServerImpl(IRepository<Long, Food> foodRepository, IRepository<Pair<Long, Long>, CatFood> catFoodRepository) {
        this.foodRepository = foodRepository;
        this.catFoodRepository = catFoodRepository;
    }


    @Override
    public void addFood(String name, String producer, Date expirationDate) {
        long id = 0;
        for (Food food:this.foodRepository.findAll())
            id = max(id, food.getId()+1);
        Food foodToBeAdded = new Food(id, name, producer, expirationDate);
        foodRepository.save(foodToBeAdded);
    }

    @Override
    public Set<Food> getFoodFromRepository() {
        return (Set<Food>) foodRepository.findAll();
    }


    @Override
    public void deleteFood(Long id) {
        StreamSupport.stream(catFoodRepository.findAll().spliterator(), false)
                .filter(catFood -> catFood.getFoodId().equals(id))
                .findAny()
                .ifPresent((catFood) -> {
                    throw new PetShopException("Food is currently eaten");
                });
        foodRepository.delete(id).orElseThrow(() -> new PetShopException("Food does not exist"));
    }

    @Override
    public void updateFood(Long id, String name, String producer, Date expirationDate) {
        foodRepository.update(new Food(id, name, producer, expirationDate))
                .orElseThrow(() -> new PetShopException("Food does not exist"));
    }
}
