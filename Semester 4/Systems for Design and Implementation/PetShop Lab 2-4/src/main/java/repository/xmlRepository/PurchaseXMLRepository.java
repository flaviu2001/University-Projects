package repository.xmlRepository;

import domain.Pair;
import domain.Purchase;
import domain.exceptions.PetShopException;
import domain.validators.Validator;
import org.w3c.dom.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PurchaseXMLRepository extends XMLRepository<Pair<Long, Long>, Purchase> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public PurchaseXMLRepository(Validator<Purchase> validator, String filePath) {
        super(validator, filePath, "purchases");
    }


    /**
     * Extracts Purchase from Element
     * @param dataTransferObject is the intermediary between data source and the program
     * @return Purchase
     */
    @Override
    protected Purchase extractEntity(Element dataTransferObject) {
        String stringId = dataTransferObject.getAttribute("id");
        List<String> idList = Arrays.asList(stringId.split(","));
        int price = Integer.parseInt(dataTransferObject.getElementsByTagName("price").item(0).getTextContent());
        String dateAcquiredString = dataTransferObject.getElementsByTagName("dateAcquired").item(0).getTextContent();
        Date dateAcquired;
        try {
            dateAcquired = dateFormat.parse(dateAcquiredString);
        } catch (ParseException parseException) {
            throw new PetShopException("Parse exception: " + parseException.getMessage());
        }
        int review = Integer.parseInt(dataTransferObject.getElementsByTagName("review").item(0).getTextContent());
        return new Purchase(
                Long.parseLong(idList.get(0)),
                Long.parseLong(idList.get(1)),
                price,
                dateAcquired,
                review
        );
    }

    /**
     * Gets Element from Purchase entity
     * @param entity which extends BaseEntity<ID>
     * @return Element
     */
    @Override
    protected Element convertEntity(Purchase entity) {
        Element purchaseElement = rootDocument.createElement("purchase");
        purchaseElement.setAttribute("id", entity.getCatId() + "," + entity.getCustomerId());
        addChildWithTextContent(purchaseElement, "price", String.valueOf(entity.getPrice()));
        addChildWithTextContent(purchaseElement, "dateAcquired", dateFormat.format(entity.getDateAcquired()));
        addChildWithTextContent(purchaseElement, "review", String.valueOf(entity.getReview()));
        return purchaseElement;
    }
}
