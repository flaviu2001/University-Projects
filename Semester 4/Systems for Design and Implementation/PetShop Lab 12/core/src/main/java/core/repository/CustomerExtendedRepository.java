package core.repository;

import core.domain.Purchase;

import java.util.List;

public interface CustomerExtendedRepository {
    List<Purchase> findPurchasesOfCustomerByCustomerIdJPQL(long customerId);

    List<Purchase> findPurchasesOfCustomerByCustomerIdCriteria(long customerId);

    List<Purchase> findPurchasesOfCustomerByCustomerIdNative(long customerId);
}
