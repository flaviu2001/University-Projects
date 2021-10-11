package core.service;

import core.domain.*;
import core.exceptions.PetShopException;
import core.repository.ICatFoodRepository;
import core.repository.ICatRepository;
import core.repository.IFoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CatFoodServiceImpl implements ICatFoodService {
    public static final Logger logger = LoggerFactory.getLogger(CatFoodServiceImpl.class);

    @Autowired
    private ICatRepository catsRepository;
    @Autowired
    private ICatFoodRepository catFoodRepository;
    @Autowired
    private IFoodRepository foodRepository;

    @Override
    public void addCatFood(Long catId, Long foodId) {
        logger.trace("add catFood - method entered - catId: " + catId + ", foodId: " + foodId);
        Optional<Cat> cat = catsRepository.findById(catId);
        cat.ifPresentOrElse((Cat c) -> {
            Optional<Food> food = foodRepository.findById(foodId);
            food.ifPresentOrElse((Food f) -> {
                CatFood catFood = new CatFood(new CatFoodPrimaryKey(catId, foodId), c, f);
                catFoodRepository.save(catFood);
            }, () -> {
                throw new PetShopException("Food id does not exist!");
            });
        }, () -> {
            throw new PetShopException("Cat id does not exist!");
        });
        logger.trace("add catFood - method finished");
    }

    @Override
    public List<CatFood> getCatFoodFromRepository() {
        logger.trace("getCatFoodFromRepository - method entered");
        List<CatFood> catFoods = catFoodRepository.findAll();
        logger.trace("getCatFoodFromRepository: " + catFoods);
        return catFoods;
    }


    @Override
    public List<Pair<Cat, Food>> getCatFoodJoin() {
        logger.trace("getCatFoodJoin - method entered");
        List<Pair<Cat, Food>> join = new LinkedList<>();
        getCatFoodFromRepository().forEach(catFood -> join.add(new Pair<>(
                catsRepository.findById(catFood.getCatId()).orElseThrow(() -> new PetShopException("Cat does not exist")),
                foodRepository.findById(catFood.getFoodId()).orElseThrow(() -> new PetShopException("Food does not exist"))
        )));
        logger.trace("getCatFoodJoin: " + join);
        return join;
    }

    @Override
    public void deleteCatFood(Long catId, Long foodId) {
        logger.trace("deleteCatFood - method entered - catId: " + catId + " - foodId: " + foodId);
        catFoodRepository.findById(new CatFoodPrimaryKey(catId, foodId))
                .ifPresentOrElse(
                        catFood -> catFoodRepository.deleteById(catFood.getId()),
                        () -> {throw new PetShopException("Cat food does not exist");}
                );
        logger.trace("deleteCatFood - method finished");
    }

    @Override
    @Transactional
    public void updateCatFood(Long catId, Long foodId, Long newFoodId) {
        logger.trace("updateCatFood - method entered - catId: " + catId + " - foodId: " + foodId + " - newFoodId: " + newFoodId);

        catsRepository.findById(catId).orElseThrow(() -> new PetShopException("Cat does not exist"));
        foodRepository.findById(foodId).orElseThrow(() -> new PetShopException("Food does not exist"));
        foodRepository.findById(newFoodId).orElseThrow(() -> new PetShopException("New food does not exist"));
        deleteCatFood(catId, foodId);
        addCatFood(catId, newFoodId);
        logger.trace("updateCatFood - method finished");
    }


    @Override
    public List<CatFood> filterCatsThatEatCertainFood(Long foodId) {
        logger.trace("filterCatsThatEatCertainFood - method entered - foodId: " + foodId);
        Food food = new Food();
        food.setId(foodId);
        List<CatFood> catFoods = catFoodRepository.findAllByFood(food);
        logger.trace("filterCatsThatEatCertainFood: " + catFoods.toString());
        return catFoods;
    }
}
