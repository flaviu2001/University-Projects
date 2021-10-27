package common;

import common.domain.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
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
    String GET_CAT_FOOD_PAIRS  = "getCatFoodJoin";
    String GET_CATFOOD = "getCatFoodFromRepository";
    String ADD_CATFOOD = "addCatFood";
    String DELETE_CATFOOD = "deleteCatFood";
    String UPDATE_CATFOOD = "updateCatFood";
    String REPORT_CUSTOMERS_BY_CASH = "reportCustomersSortedBySpentCash";
    String GET_CUSTOMERS = "getCustomersFromRepository";
    String ADD_CUSTOMER = "addCustomer";
    String DELETE_CUSTOMER = "deleteCustomer";
    String UPDATE_CUSTOMER = "updateCustomer";
    String FILTER_CATS_BASED_ON_FOOD = "filterCatsThatEatCertainFood";
    String FILTER_CUSTOMERS_BASED_ON_BREED_PURCHASE = "filterCustomersThatBoughtBreedOfCat";
    String ADD_PURCHASE = "addPurchase";
    String DELETE_PURCHASE = "deletePurchase";
    String UPDATE_PURCHASE = "updatePurchase";
    String GET_PURCHASES = "getPurchasesFromRepository";
    String FILTER_PURCHASES_BASED_ON_REVIEW = "filterPurchasesBasedOnReview";

    CompletableFuture<Iterable<Cat>> getCatsFromRepository();
    CompletableFuture<String> addCat(String name, String breed, Integer catYears);
    CompletableFuture<String> deleteCat(Long id);
    CompletableFuture<String> updateCat(Long id, String name, String breed, Integer catYears);
    CompletableFuture<Iterable<Food>> getFoodFromRepository();
    CompletableFuture<String> addFood(String name, String producer, Date expirationDate);
    CompletableFuture<String> deleteFood(Long id);
    CompletableFuture<String> updateFood(Long id, String name, String producer, Date expirationDate);
    CompletableFuture<Iterable<Pair<Cat, Food>>> getCatFoodJoin();
    CompletableFuture<Iterable<CatFood>> getCatFoodFromRepository();
    CompletableFuture<String> addCatFood(Long catId, Long foodId);
    CompletableFuture<String> deleteCatFood(Long catId, Long foodId);
    CompletableFuture<String> updateCatFood(Long catId, Long foodId, Long newFoodId);
    CompletableFuture<Iterable<Pair<Customer, Integer>>> reportCustomersSortedBySpentCash();
    CompletableFuture<Iterable<Customer>> getCustomersFromRepository();
    CompletableFuture<String> addCustomer(String name, String phoneNumber);
    CompletableFuture<String> deleteCustomer(Long id);
    CompletableFuture<String> updateCustomer(Long id, String name, String phoneNumber);
    CompletableFuture<Iterable<Cat>> filterCatsThatEatCertainFood(Long foodId);
    CompletableFuture<Iterable<Customer>> filterCustomersThatBoughtBreedOfCat(String breed);
    CompletableFuture<Iterable<Purchase>> getPurchasesFromRepository();
    CompletableFuture<String> addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review);
    CompletableFuture<String> deletePurchase(Long catId, Long customerId);
    CompletableFuture<String> updatePurchase(Long catId, Long customerId, int review);
    CompletableFuture<Iterable<Purchase>> filterPurchasesBasedOnReview(int review);
}
