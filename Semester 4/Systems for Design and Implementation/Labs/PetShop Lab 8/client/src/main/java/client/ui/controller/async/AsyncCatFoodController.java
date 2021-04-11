package client.ui.controller.async;

import core.domain.Cat;
import core.domain.CatFood;
import core.domain.Food;
import core.domain.Pair;
import core.exceptions.PetShopException;
import core.service.ICatFoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

//@Service
public class AsyncCatFoodController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncCatFoodController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private ICatFoodService catFoodService;

    public CompletableFuture<Iterable<CatFood>> getCatFoodFromRepository() {
        return CompletableFuture.supplyAsync(catFoodService::getCatFoodFromRepository, executorService);
    }

    public CompletableFuture<String> addCatFood(Long catId, Long foodId) {
        logger.trace("addCatFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                catFoodService.addCatFood(catId, foodId);
                return "Cat food added";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteCatFood(Long catId, Long foodId) {
        logger.trace("deleteCatFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                catFoodService.deleteCatFood(catId, foodId);
                return "Cat food deleted";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> updateCatFood(Long catId, Long foodId, Long newFoodId) {
        logger.trace("updateCatFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                catFoodService.updateCatFood(catId, foodId, newFoodId);
                return "Cat food updated";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Pair<Cat, Food>>> getCatFoodJoin() {
        logger.trace("getCatFoodJoin - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(catFoodService::getCatFoodJoin, executorService);
    }

    public CompletableFuture<Iterable<Cat>> filterCatsThatEatCertainFood(Long foodId) {
        logger.trace("filterCatsThatEatCertainFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> catFoodService.filterCatsThatEatCertainFood(foodId), executorService);
    }
}
