package service;

import common.domain.Cat;
import common.domain.CatFood;
import common.domain.Food;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.service.ICatFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import repository.ICatRepository;
import repository.databaseRepository.CatFoodDatabaseRepository;
import repository.databaseRepository.FoodDatabaseRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CatFoodServiceServerImpl implements ICatFoodService {
    @Autowired
    private ICatRepository catsRepository;
    final CatFoodDatabaseRepository catFoodRepository;
    final FoodDatabaseRepository foodRepository;

    public CatFoodServiceServerImpl(CatFoodDatabaseRepository catFoodRepository, FoodDatabaseRepository foodRepository) {
        this.catFoodRepository = catFoodRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    public void addCatFood(Long catId, Long foodId) {
        Optional<Cat> cat = catsRepository.findById(catId);
        cat.ifPresentOrElse((Cat c) -> {
            Optional<Food> food = foodRepository.findOne(foodId);
            food.ifPresentOrElse((Food f) -> {
                CatFood catFood = new CatFood(catId, foodId);
                catFoodRepository.saveEntity(catFood);
            }, () -> {
                throw new PetShopException("Food id does not exist!");
            });
        }, () -> {
            throw new PetShopException("Cat id does not exist!");
        });
    }

    @Override
    public Set<CatFood> getCatFoodFromRepository() {
        return (Set<CatFood>) catFoodRepository.findAllEntities();
    }


    @Override
    public List<Pair<Cat, Food>> getCatFoodJoin() {
        List<Pair<Cat, Food>> join = new LinkedList<>();
        getCatFoodFromRepository().forEach(catFood -> join.add(new Pair<>(
                catsRepository.findById(catFood.getCatId()).orElseThrow(() -> new PetShopException("Cat does not exist")),
                foodRepository.findOne(catFood.getFoodId()).orElseThrow(() -> new PetShopException("Food does not exist"))
        )));
        return join;
    }

    @Override
    public void deleteCatFood(Long catId, Long foodId) {
        catFoodRepository.deleteEntity(new Pair<>(catId, foodId))
                .orElseThrow(() -> new PetShopException("Cat food does not exist"));
    }

    @Override
    public void updateCatFood(Long catId, Long foodId, Long newFoodId) {
        catsRepository.findById(catId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        foodRepository.findOne(foodId).orElseThrow(() -> new PetShopException("Food does not exist"));
        foodRepository.findOne(newFoodId).orElseThrow(() -> new PetShopException("New food does not exist"));
        catFoodRepository.deleteEntity(new Pair<>(catId, foodId))
                .orElseThrow(() -> new PetShopException("Cat food does not exist"));
        addCatFood(catId, newFoodId);
    }


    @Override
    public List<Cat> filterCatsThatEatCertainFood(Long foodId) {
        return catsRepository.findAll().stream()
                .filter(
                        cat -> getCatFoodFromRepository().stream().anyMatch(
                                catFood -> catFood.getCatId().equals(cat.getId()) &&
                                        catFood.getFoodId().equals(foodId)
                        )
                )
                .collect(Collectors.toList());
    }
}
