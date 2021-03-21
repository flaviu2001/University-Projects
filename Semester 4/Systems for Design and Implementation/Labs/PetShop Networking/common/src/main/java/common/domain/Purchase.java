package common.domain;

import java.util.Date;

public class Purchase extends BaseEntity<Pair<Long, Long>> {
    private int price;
    private Date dateAcquired;
    private int review;

    public Purchase(Long catId, Long customerId, int price, Date dateAcquired, int review) {
        this.setId(new Pair<>(catId, customerId));
        this.price = price;
        this.dateAcquired = dateAcquired;
        this.review = review;
    }

    public Long getCatId() {
        return getId().getLeft();
    }

    public Long getCustomerId() {
        return getId().getRight();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDateAcquired() {
        return dateAcquired;
    }

    public void setDateAcquired(Date dateAcquired) {
        this.dateAcquired = dateAcquired;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
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
