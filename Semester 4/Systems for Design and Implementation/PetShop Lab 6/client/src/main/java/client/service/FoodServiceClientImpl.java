package client.service;

import common.domain.Food;
import common.service.IFoodService;
import common.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Set;

public class FoodServiceClientImpl implements IFoodService {
    @Autowired
    private IFoodService foodService;

    /**
     * Saves the food with the given attributes to the food repository.
     *
     * @param name name of food
     * @param producer producer of food
     * @param expirationDate expiration date of food
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the food entity is not valid.
     */
    @Override
    public void addFood(String name, String producer, Date expirationDate) {
        foodService.addFood(name, producer, expirationDate);
    }

    /**
     * @return all the food from the repository.
     */
    @Override
    public Set<Food> getFoodFromRepository() {
        return foodService.getFoodFromRepository();
    }

    /**
     * Deletes a food based on it's id
     *
     * @param id - id of the food to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the food does not exist
     *                                  if the food is currently eaten
     */
    @Override
    public void deleteFood(Long id) {
        foodService.deleteFood(id);
    }

    /**
     * Updates the food with the given attributes.
     *
     * @param id             must not be null
     * @param name name of food
     * @param producer producer of food
     * @param expirationDate expiration date of food
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the food entity is not valid.
     */
    @Override
    public void updateFood(Long id, String name, String producer, Date expirationDate) {
        foodService.updateFood(id, name, producer, expirationDate);
    }
}
