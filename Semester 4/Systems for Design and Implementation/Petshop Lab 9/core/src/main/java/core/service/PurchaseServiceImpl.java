package core.service;

import core.domain.*;
import core.exceptions.PetShopException;
import core.repository.ICatRepository;
import core.repository.ICustomerRepository;
import core.repository.IPurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements IPurchaseService {
    public static final Logger logger = LoggerFactory.getLogger(CatFoodServiceImpl.class);

    @Autowired
    private IPurchaseRepository purchaseRepository;

    @Autowired
    private ICatRepository catsRepository;

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        logger.trace("addPurchase - method entered - catId: " + catId + ", customerId: " + customerId + ", price: " + price + ", date acquired: " + dateAcquired + ", review: " + review);
        Optional<Cat> cat = catsRepository.findById(catId);
        cat.ifPresentOrElse((Cat c) -> {
            Optional<Customer> customer = customerRepository.findById(customerId);
            customer.ifPresentOrElse((Customer cust) -> {
                Purchase purchase = new Purchase(
                        new CustomerPurchasePrimaryKey(cust.getId(), c.getId()),
                        cust,
                        c,
                        price,
                        dateAcquired,
                        review
                );
                System.out.println(purchase);
                purchaseRepository.save(purchase);
            }, () -> {
                throw new PetShopException("Customer id does not exist");
            });
        }, () -> {
            throw new PetShopException("Cat id does not exist");
        });
        logger.trace("addPurchase - method finished");

    }

    @Override
    public List<Purchase> getPurchasesFromRepository() {
        logger.trace("getPurchasesFromRepository - method entered");
        List<Purchase> purchases = purchaseRepository.findAll();
        logger.trace("getPurchasesFromRepository: " + purchases);

        return purchases;
    }


    @Override
    public void deletePurchase(Long catId, Long customerId) {
        logger.trace("delete purchase - method entered - catId: " + catId + ", customerId: " + customerId);
        purchaseRepository.findById(new CustomerPurchasePrimaryKey(customerId, catId))
                .ifPresentOrElse(
                        purchase -> purchaseRepository.deleteById(purchase.getId()),
                        () -> {throw new PetShopException("Purchase does not exist");}
                );
        logger.trace("delete purchase - method finished");
    }

    @Override
    @Transactional
    public void updatePurchase(Long catId, Long customerId, int newReview) {
        logger.trace("updatePurchase - method entered - catId: " + catId + ", customerId: " + customerId + " ,newReview" + newReview);
        purchaseRepository.findById(new CustomerPurchasePrimaryKey(customerId, catId))
                .ifPresentOrElse(
                        (purchase) -> purchase.setReview(newReview),
                        () -> {throw new PetShopException("Purchase does not exist");}
                );
        logger.trace("updatePurchase - method finished");
    }

    @Override
    public Set<Customer> filterCustomersThatBoughtBreedOfCat(String breed) {

        logger.trace("filterCustomersThatBoughBreedOfCat - method entered - breed: " + breed);
        Set<Customer> purchases = purchaseRepository.findAllByCat_Breed(breed).stream()
                .map(Purchase::getCustomer)
                .collect(Collectors.toSet());
        logger.trace("filterCustomersThatBoughBreedOfCat - method finished - " + purchases);
        return purchases;
    }

    @Override
    public List<Purchase> filterPurchasesWithMinStars(int minStars) {
        logger.trace("filterPurchasesWithMinStars - method entered - min stars: " + minStars);
        List<Purchase> purchases = purchaseRepository.findAllByReviewGreaterThanEqual(minStars);
        logger.trace("filterPurchasesWithMinStars - method finished - " + purchases);
        return purchases;
    }

    @Override
    public List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash() {
        logger.trace("reportCustomersSortedBySpentCash - method entered");
        List<Pair<Customer, Integer>> toReturn = customerRepository.getCustomersSortedSpentCashInterface().stream()
                .map(obj -> new Pair<>(new Customer(((BigInteger)obj[0]).longValue(), (String)obj[1], (String)obj[2]), ((BigInteger)obj[3]).intValue()))
                .collect(Collectors.toList());
        logger.trace("reportCustomersSortedBySpentCash - method finished - " + toReturn);
        return toReturn;
    }
}
