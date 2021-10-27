import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManip {
    public static List<EnhancedProduct> getProducts() {
        Scanner reader;
        try {
            reader = new Scanner(new BufferedInputStream(new FileInputStream("products.txt")));
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        List<EnhancedProduct> products = new ArrayList<>();
        int n = reader.nextInt();
        for (int i = 0; i < n; ++i) {
            String name = reader.next();
            int quantity = reader.nextInt();
            int price = reader.nextInt();
            products.add(new EnhancedProduct(name, quantity, price));
        }
        return products;
    }
}
