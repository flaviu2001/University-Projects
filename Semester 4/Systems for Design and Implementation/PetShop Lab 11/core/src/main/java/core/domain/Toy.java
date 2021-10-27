package core.domain;

import javax.persistence.*;

@Entity
public class Toy extends BaseEntity<Long>{
    String name;
    int price;

    @ManyToOne
    private Cat cat;

    public Toy() {
    }

    public Toy(Long id, String name, int price) {
        this.setId(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Cat getCat() {
        return cat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
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
