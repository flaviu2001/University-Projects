package core.service;

import core.domain.*;
import core.exceptions.PetShopException;
import core.repository.CatRepository;
import core.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    public static final Logger logger = LoggerFactory.getLogger(Purchase.class);

    @Autowired
    private CatRepository catsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        logger.trace("addPurchase - method entered - catId: " + catId + ", customerId: " + customerId + ", price: " + price + ", date acquired: " + dateAcquired + ", review: " + review);
        Optional<Cat> cat = catsRepository.findById(catId);
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (cat.isPresent() && customer.isPresent()) {
            cat.get().addPurchase(customer.get(), price, dateAcquired, review);
//            customer.get().addPurchase(cat.get(), price, dateAcquired, review);
        } else throw new PetShopException("Invalid purchase");
        logger.trace("addPurchase - method finished");

    }

    @Override
    public List<Purchase> getPurchasesFromRepository() {
        logger.trace("getPurchasesFromRepository - method entered");
        List<Purchase> purchases = new ArrayList<>();
        for (var cat : catsRepository.findAll())
            purchases.addAll(cat.getPurchases());
        logger.trace("getPurchasesFromRepository: " + purchases);
        return purchases;
    }

    @Override
    public List<Pair<Customer, Integer>> reportCustomersSortedBySpentCash() {
        logger.trace("reportCustomersSortedBySpentCash - method entered");
        List<Pair<Customer, Integer>> toReturn = customerRepository.getCustomersSortedSpentCashInterface().stream()
                .map(obj -> {
                    var customer = new Customer(((BigInteger)obj[0]).longValue(), (String)obj[1], (String)obj[4]);
                    if (obj[2] != null && obj[3] != null)
                        customer.setPetHouse(new PetHouse((Integer) obj[3], (String) obj[2]));
                    return new Pair<>(customer, ((BigInteger) obj[5]).intValue());
                })
                .collect(Collectors.toList());
        logger.trace("reportCustomersSortedBySpentCash - method finished - " + toReturn);
        return toReturn;
    }
}
