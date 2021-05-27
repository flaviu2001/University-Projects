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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.CatDTO;
import web.dto.CatFoodDTO;
import web.dto.CatFoodPrimaryKeyDTO;
import web.dto.CatFoodsDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncCatFoodController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncCatFoodController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<CatFood>> getCatFoodFromRepository() {
        return CompletableFuture.supplyAsync(
                () ->{
                    try{
                        String url = "http://localhost:8080/api/catFoods";
                        CatFoodsDTO catFoodsDTO = restTemplate.getForObject(url, CatFoodsDTO.class);
                        if(catFoodsDTO == null)
                            throw new PetShopException("Could not retrieve cat foods from server");
                        return catFoodsDTO.getCatFoods().stream()
                                .map(DTO -> new CatFood(DTO.getId(), DTO.getCat(), DTO.getFood()))
                                .collect(Collectors.toSet());
                    }catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }

    public CompletableFuture<String> addCatFood(Long catId, Long foodId) {
        logger.trace("addCatFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/catFoods";
                restTemplate.postForObject(url,
                        new CatFoodPrimaryKeyDTO(catId, foodId),
                        CatFoodPrimaryKeyDTO.class);
                return "CatFood added";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteCatFood(Long catId, Long foodId) {
        logger.trace("deleteCatFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/catFoods";
                restTemplate.delete(url + "/{catId}&{foodId}", catId, foodId);
                return "CatFood deleted";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }


    public CompletableFuture<String> updateCatFood(Long catId, Long foodId, Long newFoodId) {
        logger.trace("updateCatFood - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/catFoods";
                restTemplate.put(url + "/{newId}", new CatFoodPrimaryKeyDTO(catId, foodId), newFoodId);
                return "Cat food successfully updated";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }


    public CompletableFuture<Iterable<CatFood>> filterCatsThatEatCertainFood(Long foodId) {
        return CompletableFuture.supplyAsync(
                () ->{
                    try{
                        String url = "http://localhost:8080/api/catFoods";
                        CatFoodsDTO catFoodsDTO = restTemplate.getForObject(url + "/" + foodId, CatFoodsDTO.class);
                        if(catFoodsDTO == null)
                            throw new PetShopException("Could not retrieve cat foods from server");
                        return catFoodsDTO.getCatFoods().stream()
                                .map(DTO -> new CatFood(DTO.getId(), DTO.getCat(), DTO.getFood()))
                                .collect(Collectors.toList());
                    }catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }

}
