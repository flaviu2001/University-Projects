package core.domain;

import javax.persistence.Embeddable;

@Embeddable
public class PetHouse {
    Integer size;
    String color;

    public PetHouse() {
    }

    public PetHouse(Integer size, String color) {
        this.size = size;
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "PetHouse{" +
                "size=" + size +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PetHouse && this.size.equals(((PetHouse) obj).getSize()) && this.color.equals(((PetHouse) obj).getColor());
    }
}
