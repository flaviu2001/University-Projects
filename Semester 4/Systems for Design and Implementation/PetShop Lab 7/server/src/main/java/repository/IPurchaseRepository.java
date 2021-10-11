package repository;

import common.domain.CustomerPurchasePrimaryKey;
import common.domain.Purchase;

public interface IPurchaseRepository extends IRepository<Purchase, CustomerPurchasePrimaryKey> {
}
