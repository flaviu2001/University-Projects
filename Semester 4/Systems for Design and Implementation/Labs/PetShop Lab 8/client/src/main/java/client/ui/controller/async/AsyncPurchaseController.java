package client.ui.controller.async;

import core.domain.CatFood;
import core.domain.Customer;
import core.domain.Pair;
import core.domain.Purchase;
import core.exceptions.PetShopException;
import core.service.IPurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.*;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncPurchaseController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncCatFoodController.class);

    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<Purchase>> getPurchasesFromRepository() {
        return CompletableFuture.supplyAsync(
                ()->{
                    try{
                        String url = "http://localhost:8080/api/purchases";
                        PurchasesDTO purchasesDTO = restTemplate.getForObject(url, PurchasesDTO.class);
                        if(purchasesDTO == null)
                            throw new PetShopException("Could not retrieve purchases from server");
                        return purchasesDTO.getPurchases().stream()
                                .map(DTO -> new Purchase(
                                        DTO.getId(),
                                        DTO.getCustomer(),
                                        DTO.getCat(),
                                        DTO.getPrice(),
                                        DTO.getDateAcquired(),
                                        DTO.getReview()
                                        ))
                                .collect(Collectors.toSet());
                    }catch (ResourceAccessException resourceAccessException) {
                        throw new PetShopException("Inaccessible server");
                    }
                }
                , executorService);
    }

    public CompletableFuture<String> addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        logger.trace("addPurchase - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/purchases";
                restTemplate.postForObject(url,
                        new CustomerPurchasePrimaryKeyDTO(
                                catId,
                                customerId,
                                price,
                                dateAcquired,
                                review
                        ),
                        CustomerPurchasePrimaryKeyDTO.class);
                return "Purchase added";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deletePurchase(Long catId, Long customerId) {
        logger.trace("deletePurchase - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/purchases";
                restTemplate.delete(url + "/{catId}&{customerId}", catId, customerId);
                return "Purchase deleted";
            } catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> updatePurchase(Long catId, Long customerId, int review) {
        logger.trace("updatePurchase - method entered and returned a completable future");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/purchases";
                restTemplate.put(url + "/{newReview}", new SimplePurchasePrimaryKeyDTO(catId, customerId), review);
                return "Purchase updated";
            } catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }


    /*
    public CompletableFuture<Iterable<Customer>> filterCustomersThatBoughtBreedOfCat(String breed) {
        return CompletableFuture.supplyAsync(() -> purchaseService.filterCustomersThatBoughtBreedOfCat(breed), executorService);
    }

    public CompletableFuture<Iterable<Purchase>> filterPurchasesWithMinStars(int minStars) {
        return CompletableFuture.supplyAsync(() -> purchaseService.filterPurchasesWithMinStars(minStars), executorService);
    }

    public CompletableFuture<Iterable<Pair<Customer, Integer>>> reportCustomersSortedBySpentCash() {
        return CompletableFuture.supplyAsync(purchaseService::reportCustomersSortedBySpentCash, executorService);
    }
     */
}
