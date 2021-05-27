package core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Purchase extends BaseEntity<CustomerPurchasePrimaryKey> {
    public  Purchase(){

    }

    private int price;
    private Date dateAcquired;
    private int review;

    public Purchase(CustomerPurchasePrimaryKey customerPurchasePrimaryKey, Date dateAcquired, int price, int review) {
        this.setId(customerPurchasePrimaryKey);
        this.price = price;
        this.dateAcquired = dateAcquired;
        this.review = review;
    }

    public boolean equals(Object obj) {
        return obj instanceof Purchase && this.getId().equals(((Purchase) obj).getId());
    }

    @Override
    public String toString() {
        return super.toString() + " Purchase{" +
                "price=" + price +
                ", dateAcquired=" + dateAcquired +
                ", review=" + review +
                '}';
    }
}
