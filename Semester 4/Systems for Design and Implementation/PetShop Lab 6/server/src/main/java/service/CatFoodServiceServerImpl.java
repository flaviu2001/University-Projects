package service;

import common.domain.Cat;
import common.domain.CatFood;
import common.domain.Food;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.service.ICatFoodService;
import repository.IRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CatFoodServiceServerImpl implements ICatFoodService {
    final IRepository<Pair<Long, Long>, CatFood> catFoodRepository;
    final IRepository<Long, Cat> catsRepository;
    final IRepository<Long, Food> foodRepository;

    public CatFoodServiceServerImpl(IRepository<Pair<Long, Long>, CatFood> catFoodRepository, IRepository<Long, Cat> catsRepository, IRepository<Long, Food> foodRepository) {
        this.catFoodRepository = catFoodRepository;
        this.catsRepository = catsRepository;
        this.foodRepository = foodRepository;
    }

    @Override
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

    @Override
    public Set<CatFood> getCatFoodFromRepository() {
        return (Set<CatFood>) catFoodRepository.findAll();
    }


    @Override
    public List<Pair<Cat, Food>> getCatFoodJoin() {
        List<Pair<Cat, Food>> join = new LinkedList<>();
        getCatFoodFromRepository().forEach(catFood -> join.add(new Pair<>(
                catsRepository.findOne(catFood.getCatId()).orElseThrow(() -> new PetShopException("Cat does not exist")),
                foodRepository.findOne(catFood.getFoodId()).orElseThrow(() -> new PetShopException("Food does not exist"))
        )));
        return join;
    }

    @Override
    public void deleteCatFood(Long catId, Long foodId) {
        catFoodRepository.delete(new Pair<>(catId, foodId))
                .orElseThrow(() -> new PetShopException("Cat food does not exist"));
    }

    @Override
    public void updateCatFood(Long catId, Long foodId, Long newFoodId) {
        catsRepository.findOne(catId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        foodRepository.findOne(foodId).orElseThrow(() -> new PetShopException("Food does not exist"));
        foodRepository.findOne(newFoodId).orElseThrow(() -> new PetShopException("New food does not exist"));
        catFoodRepository.delete(new Pair<>(catId, foodId))
                .orElseThrow(() -> new PetShopException("Cat food does not exist"));
        addCatFood(catId, newFoodId);
    }


    @Override
    public List<Cat> filterCatsThatEatCertainFood(Long foodId) {
        return StreamSupport.stream(catsRepository.findAll().spliterator(), false)
                .filter(
                        cat -> getCatFoodFromRepository().stream().anyMatch(
                                catFood -> catFood.getCatId().equals(cat.getId()) &&
                                        catFood.getFoodId().equals(foodId)
                        )
                )
                .collect(Collectors.toList());
    }
}
