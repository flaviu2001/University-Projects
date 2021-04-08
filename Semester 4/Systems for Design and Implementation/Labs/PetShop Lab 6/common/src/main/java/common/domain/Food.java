package common.domain;

import java.util.Date;

public class Food extends BaseEntity<Long>{
    String name, producer;
    Date expirationDate;


    public Food(Long id, String name, String producer, Date expirationDate) {
        this.setId(id);
        this.name = name;
        this.producer = producer;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return super.toString() +
                " Food{name: " + this.name +
                "; producer: " + this.producer +
                "; expirationDate: " + this.expirationDate +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Food && this.getId().equals(((Food) obj).getId());
    }
}
