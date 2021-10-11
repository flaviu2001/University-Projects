package client.service;

import common.domain.Customer;
import common.domain.Pair;
import common.domain.Purchase;
import common.service.IFoodService;
import common.service.IPurchaseService;
import common.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class PurchaseServiceClientImpl implements IPurchaseService {
    @Autowired
    private IPurchaseService purchaseService;

    /**
     * Saves the purchase with the given attributes to the repository of purchases
     *
     * @param catId        must not be null
     * @param customerId   must not be null
     * @param price        price of the purchase
     * @param dateAcquired date when it was made
     * @param review       number of stars given by the customer
     * @throws PetShopException if catId or customerId does not exist
     */
    @Override
    public void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        purchaseService.addPurchase(catId, customerId, price, dateAcquired, review);
    }

    /**
     * @return all purchases from the repository
     */
    @Override
    public Set<Purchase> getPurchasesFromRepository() {
        return purchaseService.getPurchasesFromRepository();
    }

    /**
     * Deletes a purchase based on id
     *
     * @param catId      must not be null
     * @param customerId must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the purchase does not exist
     */
    @Override
    public void deletePurchase(Long catId, Long customerId) {
        purchaseService.deletePurchase(catId, customerId);
    }

    /**
     * @param catId      must not be null
     * @param customerId must not be null
     * @param newReview new review of purchase
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the purchase entity is not valid.
     * @throws PetShopException         if the purchase does not exist / cat does not exist / customer does not exist
     */
    @Override
    public void updatePurchase(Long catId, Long customerId, int newReview) {
        purchaseService.updatePurchase(catId, customerId, newReview);
    }

    /**
     * @param breed - the breed of cat by which to filter
     * @return a list of all customers who bought at least a cat of a certain breed
     */
    @Override
    public List<Customer> filterCustomersThatBoughtBreedOfCat(String breed) {
        return purchaseService.filterCustomersThatBoughtBreedOfCat(breed);
    }

    /**
     * @param minStars - the minimum amount of stars by which you filter, must be between 1 and 5
     * @return a list of all purchases with a minimum amount of stars
     */
    @Override
    public List<Purchase> filterPurchasesWithMinStars(int minStars) {
        return purchaseService.filterPurchasesWithMinStars(minStars);
    }

    /**
     * @return a list of Customer - Integer pairs with spent money from each customer sorted by money spent
     */
    @Override
    public List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash() {
        return purchaseService.reportCustomersSortedBySpentCash();
    }
}
