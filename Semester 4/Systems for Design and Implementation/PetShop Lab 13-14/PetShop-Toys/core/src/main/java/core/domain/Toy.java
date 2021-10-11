package core.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Toy extends BaseEntity<Long> {
    String name;
    int price;
    long catId;

    public Toy() {
    }

    public Toy(Long id, String name, int price) {
        this.setId(id);
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Toy && this.getId().equals(((Toy) obj).getId());
    }
}
