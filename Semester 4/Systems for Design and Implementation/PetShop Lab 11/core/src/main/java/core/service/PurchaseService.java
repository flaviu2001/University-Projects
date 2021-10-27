package core.service;

import core.domain.Customer;
import core.domain.Pair;
import core.domain.Purchase;
import core.exceptions.PetShopException;

import java.util.Date;
import java.util.List;

public interface PurchaseService {
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
    void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review);


    /**
     * @return all purchases from the repository
     */
    List<Purchase> getPurchasesFromRepository();

    /**
     * @return a list of Customer - Integer pairs with spent money from each customer sorted by money spent
     */
    List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash();
}
