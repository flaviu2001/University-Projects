package common;

import common.domain.Cat;
import common.domain.Food;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public interface PetShopService {
    int PORT = 1234;
    String HOST = "localhost";
    String GET_CATS = "getCatsFromRepository";
    String ADD_CAT = "addCat";
    String DELETE_CAT = "deleteCat";
    String UPDATE_CAT = "updateCat";
    String GET_FOOD = "getFoodFromRepository";
    String ADD_FOOD = "addFood";
    String DELETE_FOOD = "deleteFood";
    String UPDATE_FOOD = "updateFood";

    CompletableFuture<Iterable<Cat>> getCatsFromRepository();
    CompletableFuture<String> addCat(String name, String breed, Integer catYears);
    CompletableFuture<String> deleteCat(Long id);
    CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears);
    CompletableFuture<Iterable<Food>> getFoodFromRepository();
    CompletableFuture<String> addFood(String name, String producer, Date expirationDate);
    CompletableFuture<String> deleteFood(Long id);
    CompletableFuture<String> updateFood(Long id, String name, String producer, Date expirationDate);
}
