package core.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CatFoodPrimaryKey implements Serializable {
    Long catId, foodId;

    public CatFoodPrimaryKey(){}

    public CatFoodPrimaryKey(Long catId, Long foodId) {
        this.catId = catId;
        this.foodId = foodId;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatFoodPrimaryKey that = (CatFoodPrimaryKey) o;
        return catId.equals(that.catId) &&
                foodId.equals(that.foodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catId, foodId);
    }

    @Override
    public String toString() {
        return "CatFoodPrimaryKey{" +
                "catId=" + catId +
                ", foodId=" + foodId +
                '}';
    }
}
