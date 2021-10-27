import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EnhancedProduct extends Product {
    private int soldQuantity;
    private final Lock mutex;

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public Lock getMutex() {
        return mutex;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public EnhancedProduct(String name, int price, int quantity) {
        super(name, quantity, price);
        this.mutex = new ReentrantLock(false);
        this.soldQuantity = 0;
    }
}
