package core.repository;

import core.domain.CustomerPurchasePrimaryKey;
import core.domain.Purchase;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends Repository<Purchase, CustomerPurchasePrimaryKey> {
    @Query(value = "select p.customerid as purchase, sum(p.price) as totalCash from purchase p " +
            "group by p.customerid " +
            "order by sum(p.price) desc", nativeQuery = true)
    List<Object[]> getPurchasesSortedSpentCashInterface();
}
