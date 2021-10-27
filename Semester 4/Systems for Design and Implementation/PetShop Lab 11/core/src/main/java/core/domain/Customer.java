package core.domain;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer extends BaseEntity<Long> {

    String name;
    String phoneNumber;

    @Embedded
    PetHouse petHouse;

    public Customer() {

    }

    public Customer(Long id, String name, String phoneNumber) {
        this.setId(id);
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<Purchase> purchases;

    public Set<Purchase> getPurchases() {
        purchases = purchases == null ? new HashSet<>() : purchases;
        return Collections.unmodifiableSet(purchases);
    }

    public void addPurchase(Cat cat, int price, Date date, int review) {
        Purchase purchase = new Purchase(new CustomerPurchasePrimaryKey(this.getId(), cat.getId()), this, cat, price, date, review);
        purchases.add(purchase);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PetHouse getPetHouse() {
        return petHouse;
    }

    public void setPetHouse(PetHouse petHouse) {
        this.petHouse = petHouse;
    }

    @Override
    public String toString() {
        return super.toString() + " Customer{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Customer && this.getId().equals(((Customer) obj).getId());
    }
}
