package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class PurchaseTest {
    private static final Long catId = 1L;
    private static final Long newCatId = 2L;
    private static final Long customerId = 3L;
    private static final Long newCustomerId = 4L;
    private static final int price = 12;
    private static final int newPrice = 13;
    private static Date currentDate;
    private static Date newDate;
    private static final int review = 4;
    private static final int newReview = 5;

    private Purchase purchase;

    @BeforeEach
    public void setUp() {
        currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.OCTOBER, 28);
        newDate = calendar.getTime();

        purchase = new Purchase(catId, customerId, price, currentDate, review);
    }

    @AfterEach
    public void tearDown() {
        purchase = null;
    }

    @Test
    public void testGetPrice() {
        Assertions.assertEquals(purchase.getPrice(), price);
    }
    @Test
    public void testSetPrice() {
        purchase.setPrice(newPrice);
        Assertions.assertEquals(newPrice, purchase.getPrice());
    }
    @Test
    public void testGetDate() {
        Assertions.assertEquals(purchase.getDateAcquired(), currentDate);
    }

    @Test
    public void testSetDate() {
        purchase.setDateAcquired(newDate);
        Assertions.assertEquals(newDate, purchase.getDateAcquired());
    }

    @Test
    public void testGetReview() {
        Assertions.assertEquals(purchase.getReview(), review);
    }

    @Test
    public void testSetReview() {
        purchase.setReview(newReview);
        Assertions.assertEquals(newReview, purchase.getReview());
    }
    @Test
    public void testEqualForDifferentPurchasesShouldReturnFalse() {
        Purchase newPurchase = new Purchase(newCatId, newCustomerId, price, currentDate, review);
        Assertions.assertNotEquals(newPurchase, purchase);
    }

    @Test
    public void testEqualForSamePurchasesShouldReturnTrue() {
        Purchase newPurchase = new Purchase(catId, customerId, price, currentDate, review);
        Assertions.assertEquals(purchase, newPurchase);
    }

}
