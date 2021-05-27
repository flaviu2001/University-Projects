package client.service;

import common.domain.Cat;
import common.domain.CatFood;
import common.domain.Food;
import common.domain.Pair;
import common.service.ICatFoodService;
import common.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class CatFoodServiceClientImpl implements ICatFoodService {
    @Autowired
    private ICatFoodService catFoodService;

    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param catId  must not be null
     * @param foodId must not be null
     * @throws PetShopException if catId or foodId does not exist
     */
    @Override
    public void addCatFood(Long catId, Long foodId) {
        catFoodService.addCatFood(catId, foodId);
    }

    /**
     * @return the cats with the food they eat from the repository
     */
    @Override
    public Set<CatFood> getCatFoodFromRepository() {
        return catFoodService.getCatFoodFromRepository();
    }

    /**
     * @return the join between cats and the food they eat
     * @throws PetShopException if there are cat foods for nonexistent cats or foods
     */
    @Override
    public List<Pair<Cat, Food>> getCatFoodJoin() {
        return catFoodService.getCatFoodJoin();
    }

    /**
     * Deletes the CatFood with the id composed of catId and foodId
     *
     * @param catId  - id of the cat
     * @param foodId - if of the food
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat food does not exist
     */
    @Override
    public void deleteCatFood(Long catId, Long foodId) {
        catFoodService.deleteCatFood(catId, foodId);
    }

    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param catId     must not be null
     * @param foodId    must not be null
     * @param newFoodId id of new food
     * @throws PetShopException if:
     *                          catId or foodId does not exist
     *                          old Cat food does not exist
     *                          new food does not exist
     */
    @Override
    public void updateCatFood(Long catId, Long foodId, Long newFoodId) {
        catFoodService.updateCatFood(catId, foodId, newFoodId);
    }

    /**
     * @param foodId - identifies the required food
     * @return a list of cats that eat the required food
     */
    @Override
    public List<Cat> filterCatsThatEatCertainFood(Long foodId) {
        return catFoodService.filterCatsThatEatCertainFood(foodId);
    }
}
