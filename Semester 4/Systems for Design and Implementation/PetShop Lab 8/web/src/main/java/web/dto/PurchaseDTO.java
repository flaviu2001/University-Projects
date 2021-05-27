package web.dto;

import core.domain.Cat;
import core.domain.Customer;
import core.domain.CustomerPurchasePrimaryKey;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PurchaseDTO extends BaseDTO<CustomerPurchasePrimaryKey> {
    Customer customer;
    Cat cat;
    int price;
    Date dateAcquired;
    int review;
}
