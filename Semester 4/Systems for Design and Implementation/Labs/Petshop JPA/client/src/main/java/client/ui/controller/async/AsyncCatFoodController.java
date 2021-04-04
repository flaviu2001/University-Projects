package client.ui.controller.async;

import common.domain.Cat;
import common.domain.CatFood;
import common.domain.Food;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.service.ICatFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class AsyncCatFoodController {
    @Autowired
    ExecutorService executorService;

    @Autowired
    private ICatFoodService catFoodService;

    public CompletableFuture<Iterable<CatFood>> getCatFoodFromRepository() {
        return CompletableFuture.supplyAsync(catFoodService::getCatFoodFromRepository, executorService);
    }

    public CompletableFuture<String> addCatFood(Long catId, Long foodId) {
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
        return CompletableFuture.supplyAsync(catFoodService::getCatFoodJoin, executorService);
    }

    public CompletableFuture<Iterable<Cat>> filterCatsThatEatCertainFood(Long foodId) {
        return CompletableFuture.supplyAsync(() -> catFoodService.filterCatsThatEatCertainFood(foodId), executorService);
    }
}
