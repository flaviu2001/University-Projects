import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record Bill(List<Product> products) {
    public List<Product> getProducts() {
        return products;
    }

    public static List<Bill> getRandomBills(List<EnhancedProduct> products, int numberOfBills) {
        Random random = new Random();
        List<Bill> bills = new ArrayList<>();
        for (int i = 0; i < numberOfBills; ++i) {
            List<Product> billsProducts = new ArrayList<>();
            int productsInBill = random.nextInt(5)+1;
            for (int j = 0; j < productsInBill; ++j) {
                int productIndex = random.nextInt(products.size());
                String name = products.get(productIndex).getName();
                int price = products.get(productIndex).getPrice();
                int quantity = random.nextInt(5)+1;
                billsProducts.add(new Product(name, quantity, price));
            }
            bills.add(new Bill(billsProducts));
        }
        return bills;
    }
}
