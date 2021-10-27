package repository.csvRepository;

import domain.Pair;
import domain.Purchase;
import domain.exceptions.PetShopException;
import domain.validators.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PurchaseCSVRepository extends CSVRepository<Pair<Long, Long>, Purchase> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public PurchaseCSVRepository(Validator<Purchase> validator, String filePath) {
        super(validator, filePath);
    }

    /**
     * @param dataTransferObject is a line from the csv file
     * @return Purchase object corresponding to the string
     */
    @Override
    protected Purchase extractEntity(String dataTransferObject) {
        try {
            List<String> tokens = Arrays.asList(dataTransferObject.split(","));
            Long catId = Long.parseLong(tokens.get(0));
            Long customerId = Long.parseLong(tokens.get(1));
            int price = Integer.parseInt(tokens.get(2));
            String dateString = tokens.get(3);
            int review = Integer.parseInt(tokens.get(4));
            Date dateAcquired = dateFormat.parse(dateString);
            return new Purchase(catId, customerId, price, dateAcquired, review);
        } catch (ParseException parseException) {
            throw new PetShopException("Parse exception: " + parseException.getMessage());
        }
    }

    /**
     *
     * @param entity Purchase to be matched to a string
     * @return string in csv format containing the purchase attributes
     */
    @Override
    protected String convertEntity(Purchase entity) {
        return entity.getCatId() + "," +
                entity.getCustomerId() + "," +
                entity.getPrice() + "," +
                dateFormat.format(entity.getDateAcquired()) + "," +
                entity.getReview();
    }
}
