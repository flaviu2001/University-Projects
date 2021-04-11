package core.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CustomerPurchasePrimaryKey implements Serializable {
    Long customerId, catId;

    public CustomerPurchasePrimaryKey() {}

    public CustomerPurchasePrimaryKey(Long customerId, Long catId){
        this.customerId = customerId;
        this.catId = catId;
    }

    public Long getCustomerId(){
        return customerId;
    }

    public Long getCatId(){
        return  catId;
    }

    public void setCustomerId(Long id){
        customerId = id;
    }

    public void setCatId(Long id){
        catId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerPurchasePrimaryKey that = (CustomerPurchasePrimaryKey) o;
        return catId.equals(that.catId) &&
                customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, catId);
    }

    @Override
    public String toString() {
        return "CustomerPurchasePrimaryKey{" +
                "catId=" + catId +
                ", customerId=" + customerId +
                '}';
    }
}
