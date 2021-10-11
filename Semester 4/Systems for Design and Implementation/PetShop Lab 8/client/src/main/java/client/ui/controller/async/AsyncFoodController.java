package client.ui.controller.async;

import core.domain.Food;
import core.exceptions.PetShopException;
import core.service.IFoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.FoodDTO;
import web.dto.FoodsDto;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncFoodController {
    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<Food>> getFoodFromRepository() {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        String url = "http://localhost:8080/api/food";
                        FoodsDto food = restTemplate.getForObject(url, FoodsDto.class);
                        if (food == null) {
                            throw new PetShopException("Could not retrieve food from server");
                        }
                        return food.getFoods().stream().map(foodDTO -> new Food(foodDTO.getId(), foodDTO.getName(), foodDTO.getProducer(), foodDTO.getExpirationDate())).collect(Collectors.toSet());
                    } catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessivle server");
                    }
                }, executorService);
    }

    public CompletableFuture<String> addFood(String name, String producer, Date date) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/food";
                restTemplate.postForObject(
                        url,
                        new FoodDTO(name, producer, date),
                        FoodDTO.class
                );
                return "Food added";
            } catch (PetShopException exception) {
                return exception.getMessage();
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteFood(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/food";
                restTemplate.delete(url + "/{id}", id);
                return "Food deleted";
            } catch (PetShopException exception) {
                return exception.getMessage();
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> updateFood(Long id, String name, String producer, Date date) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/food";
                System.out.println(producer);
                FoodDTO foodToUpdate = new FoodDTO(name, producer, date);
                foodToUpdate.setId(id);
                restTemplate.put(url + "/{id}", foodToUpdate, foodToUpdate.getId());
                return "Food updated";
            } catch (PetShopException exception) {
                return exception.getMessage();
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

}
