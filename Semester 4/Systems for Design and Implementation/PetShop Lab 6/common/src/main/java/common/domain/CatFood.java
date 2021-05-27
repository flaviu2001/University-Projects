package common.domain;


public class CatFood extends BaseEntity<Pair<Long, Long>>{

    public CatFood(Long catId, Long foodId) {
        this.setId(new Pair<>(catId, foodId));
    }

    public Long getCatId() {
        return this.getId().getLeft();
    }

    public Long getFoodId() {
        return this.getId().getRight();
    }

    public void setCatId(Long catId){
        this.getId().setLeft(catId);
    }

    public void setFoodId(Long foodId){
        this.getId().setRight(foodId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CatFood && this.getId().equals(((CatFood) obj).getId());
    }
}

