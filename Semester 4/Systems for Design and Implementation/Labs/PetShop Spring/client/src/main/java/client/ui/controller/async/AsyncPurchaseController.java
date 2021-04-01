package client.ui.controller.async;

import common.domain.Customer;
import common.domain.Pair;
import common.domain.Purchase;
import common.exceptions.PetShopException;
import common.service.IPurchaseService;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AsyncPurchaseController {
    ExecutorService executorService;
    private final IPurchaseService purchaseService;

    public AsyncPurchaseController(ExecutorService executorService, IPurchaseService purchaseService) {
        this.executorService = executorService;
        this.purchaseService = purchaseService;
    }

    public CompletableFuture<Iterable<Purchase>> getPurchasesFromRepository() {
        return CompletableFuture.supplyAsync(purchaseService::getPurchasesFromRepository, executorService);
    }

    public CompletableFuture<String> addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                purchaseService.addPurchase(catId, customerId, price, dateAcquired, review);
                return "Purchase added";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> deletePurchase(Long catId, Long customerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                purchaseService.deletePurchase(catId, customerId);
                return "Purchase deleted";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<String> updatePurchase(Long catId, Long customerId, int review) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                purchaseService.updatePurchase(catId, customerId, review);
                return "Purchase updated";
            } catch (PetShopException exception) {
                return exception.getMessage();
            }
        }, executorService);
    }

    public CompletableFuture<Iterable<Customer>> filterCustomersThatBoughtBreedOfCat(String breed) {
        return CompletableFuture.supplyAsync(() -> purchaseService.filterCustomersThatBoughtBreedOfCat(breed), executorService);
    }

    public CompletableFuture<Iterable<Purchase>> filterPurchasesWithMinStars(int minStars) {
        return CompletableFuture.supplyAsync(() -> purchaseService.filterPurchasesWithMinStars(minStars), executorService);
    }

    public CompletableFuture<Iterable<Pair<Customer, Integer>>> reportCustomersSortedBySpentCash() {
        return CompletableFuture.supplyAsync(purchaseService::reportCustomersSortedBySpentCash, executorService);
    }
}
