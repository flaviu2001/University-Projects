package common.service;

import common.domain.*;
import common.exceptions.PetShopException;

import java.util.List;
import java.util.Set;

public interface ICatFoodService {
    /**
     * Saves the cat along with the food it eats to the CatFoodRepository
     *
     * @param catId  must not be null
     * @param foodId must not be null
     * @throws PetShopException if catId or foodId does not exist
     */
    void addCatFood(Long catId, Long foodId);

    /**
     * @return the cats with the food they eat from the repository
     */
    Set<CatFood> getCatFoodFromRepository();

    /**
     * @return the join between cats and the food they eat
     * @throws PetShopException if there are cat foods for nonexistent cats or foods
     */
    List<Pair<Cat, Food>> getCatFoodJoin();

    /**
     * Deletes the CatFood with the id composed of catId and foodId
     *
     * @param catId  - id of the cat
     * @param foodId - if of the food
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the cat food does not exist
     */
    void deleteCatFood(Long catId, Long foodId);

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
    void updateCatFood(Long catId, Long foodId, Long newFoodId);

    /**
     * @param foodId - identifies the required food
     * @return a list of cats that eat the required food
     */
    List<Cat> filterCatsThatEatCertainFood(Long foodId);
}

