import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Supermarket {
    private int money;
    private final List<EnhancedProduct> products;
    private final Lock moneyMutex;
    private final ReadWriteLock everythingMutex;
    private final List<Bill> consumedBillList;

    public Supermarket(List<EnhancedProduct> products) {
        this.products = products;
        this.money = 0;
        this.moneyMutex = new ReentrantLock();
        this.everythingMutex = new ReentrantReadWriteLock(false);
        this.consumedBillList = new ArrayList<>();
    }

    public void parseBill(Bill bill) {
        everythingMutex.readLock().lock();
        int totalPrice = 0;
        for (Product billProduct : bill.getProducts())
            for (EnhancedProduct product : products)
                if (billProduct.getName().equals(product.getName())) {
                    product.getMutex().lock();
                    product.setSoldQuantity(product.getSoldQuantity() + billProduct.getQuantity());
                    totalPrice += product.getPrice() * billProduct.getQuantity();
                    product.getMutex().unlock();
                }
        moneyMutex.lock();
        consumedBillList.add(bill);
        money += totalPrice;
        moneyMutex.unlock();
        everythingMutex.readLock().unlock();
    }

    public boolean checkEverything() {
        everythingMutex.writeLock().lock();
        System.out.println("Checked so far " + consumedBillList.size() + " bills");
        Map<String, Integer> checkerMap = new HashMap<>();
        for (EnhancedProduct product : products)
            checkerMap.put(product.getName(), 0);
        int totalPrice = 0;
        for (Bill bill : consumedBillList)
            for (Product product : bill.getProducts()) {
                checkerMap.put(product.getName(), checkerMap.get(product.getName()) + product.getQuantity());
                totalPrice += product.getQuantity() * product.getPrice();
            }
        if (totalPrice != money) {
            everythingMutex.writeLock().unlock();
            return false;
        }
        for (EnhancedProduct enhancedProduct : products)
            if (!checkerMap.get(enhancedProduct.getName()).equals(enhancedProduct.getSoldQuantity())) {
                everythingMutex.writeLock().unlock();
                return false;
            } else if (enhancedProduct.getSoldQuantity() > enhancedProduct.getQuantity()) {
                everythingMutex.writeLock().unlock();
                return false;
            }
        everythingMutex.writeLock().unlock();
        return true;
    }
}
