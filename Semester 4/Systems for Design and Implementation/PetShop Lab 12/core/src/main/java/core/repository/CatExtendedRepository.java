package core.repository;

import core.domain.Purchase;

import java.util.List;

public interface CatExtendedRepository {
    List<Purchase> findPurchasesOfCatByCatIdJPQL(long catId);
    List<Purchase> findPurchasesOfCatByCatIdCriteria(long catId);
    List<Purchase> findPurchasesOfCatByCatIdNative(long catId);
}
