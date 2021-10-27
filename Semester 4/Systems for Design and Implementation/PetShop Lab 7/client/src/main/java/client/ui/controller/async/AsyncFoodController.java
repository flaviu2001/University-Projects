package client.ui.controller.async;

import common.domain.Food;
import common.exceptions.PetShopException;
import common.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class AsyncFoodController {
    @Autowired
    ExecutorService executorService;

    @Autowired
    private IFoodService foodService;

    public CompletableFuture<Iterable<Food>> getFoodFromRepository() {
        return CompletableFuture.supplyAsync(foodService::getFoodFromRepository, executorService);
    }

    public CompletableFuture<String> addFood(String name, String producer, Date date) {
        return CompletableFuture.supplyAsync(() -> {
                try {
                    foodService.addFood(name, producer, date);
                    return "Food added";
                } catch (PetShopException exception) {
                    return exception.getMessage();
                }
            }, executorService);
    }

    public CompletableFuture<String> deleteFood(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                foodService.deleteFood(id);
                return "Food deleted";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> updateFood(Long id, String name, String producer, Date date) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                foodService.updateFood(id, name, producer, date);
                return "Food updated";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        },executorService);
    }

}
