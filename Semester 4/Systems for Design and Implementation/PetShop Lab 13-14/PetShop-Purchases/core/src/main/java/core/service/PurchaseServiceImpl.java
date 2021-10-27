package core.service;

import core.domain.CustomerPurchasePrimaryKey;
import core.domain.Pair;
import core.domain.Purchase;
import core.repository.PurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    public static final Logger logger = LoggerFactory.getLogger(Purchase.class);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    @Transactional
    public void addPurchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        logger.trace("addPurchase - method entered - catId: " + catId + ", customerId: " + customerId + ", price: " + price + ", date acquired: " + dateAcquired + ", review: " + review);
        purchaseRepository.save(new Purchase(new CustomerPurchasePrimaryKey(customerId, catId), dateAcquired, price, review));
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
    public List<Purchase> findPurchasesOfCatByCatId(long catId) {
        return getPurchasesFromRepository().stream().filter(purchase -> purchase.getId().getCatId() == catId).collect(Collectors.toList());
    }

    @Override
    public List<Purchase> findPurchasesOfCustomerByCustomerId(long customerId) {
        return getPurchasesFromRepository().stream().filter(purchase -> purchase.getId().getCustomerId() == customerId).collect(Collectors.toList());
    }

    @Override
    public List<Pair<Long, Integer>> getPurchasesSortedSpentCashInterface() {
        logger.trace("getPurchasesSortedSpentCashInterface - method entered");
        List<Pair<Long, Integer>> toReturn = purchaseRepository.getPurchasesSortedSpentCashInterface().stream()
                .map(obj -> new Pair<>(((BigInteger)obj[0]).longValue(), ((BigInteger)obj[1]).intValue()))
                .collect(Collectors.toList());
        logger.trace("reportCustomersSortedBySpentCash - method finished - " + toReturn);
        return toReturn;
    }
}
