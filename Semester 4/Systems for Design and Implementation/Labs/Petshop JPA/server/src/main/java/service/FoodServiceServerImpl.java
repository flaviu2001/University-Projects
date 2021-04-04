package service;

import common.domain.Food;
import common.exceptions.PetShopException;
import common.service.IFoodService;
import repository.databaseRepository.CatFoodDatabaseRepository;
import repository.databaseRepository.FoodDatabaseRepository;

import java.util.Date;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.lang.Math.max;

public class FoodServiceServerImpl implements IFoodService {
    final FoodDatabaseRepository foodRepository;
    final CatFoodDatabaseRepository catFoodRepository;

    public FoodServiceServerImpl(FoodDatabaseRepository foodRepository, CatFoodDatabaseRepository catFoodRepository) {
        this.foodRepository = foodRepository;
        this.catFoodRepository = catFoodRepository;
    }

    @Override
    public void addFood(String name, String producer, Date expirationDate) {
        long id = 0;
        for (Food food:this.foodRepository.findAllEntities())
            id = max(id, food.getId()+1);
        Food foodToBeAdded = new Food(id, name, producer, expirationDate);
        foodRepository.saveEntity(foodToBeAdded);
    }

    @Override
    public Set<Food> getFoodFromRepository() {
        return (Set<Food>) foodRepository.findAllEntities();
    }


    @Override
    public void deleteFood(Long id) {
        StreamSupport.stream(catFoodRepository.findAllEntities().spliterator(), false)
                .filter(catFood -> catFood.getFoodId().equals(id))
                .findAny()
                .ifPresent((catFood) -> {
                    throw new PetShopException("Food is currently eaten");
                });
        foodRepository.deleteEntity(id).orElseThrow(() -> new PetShopException("Food does not exist"));
    }

    @Override
    public void updateFood(Long id, String name, String producer, Date expirationDate) {
        foodRepository.update(new Food(id, name, producer, expirationDate))
                .orElseThrow(() -> new PetShopException("Food does not exist"));
    }
}
