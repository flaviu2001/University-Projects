package repository.xmlRepository;

import domain.Food;
import domain.exceptions.PetShopException;
import domain.validators.Validator;
import org.w3c.dom.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodXMLRepository extends XMLRepository<Long, Food> {
    public FoodXMLRepository(Validator<Food> validator, String filePath) {
        super(validator, filePath, "foods");
    }

    /**
     * Extracts Food from Element
     * @param dataTransferObject is the intermediary between data source and the program
     * @return Food
     */
    @Override
    protected Food extractEntity(Element dataTransferObject) {
        Long id = Long.parseLong(dataTransferObject.getAttribute("id"));
        String name = dataTransferObject.getElementsByTagName("name").item(0).getTextContent();
        String producer = dataTransferObject.getElementsByTagName("producer").item(0).getTextContent();
        String expirationDateString = dataTransferObject.getElementsByTagName("expirationDate").item(0).getTextContent();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = dateFormat.parse(expirationDateString);
        } catch (ParseException parseException) {
            throw new PetShopException("Parse exception: " + parseException.getMessage());
        }
        return new Food(id, name, producer, date);
    }

    /**
     * Gets Element from Food
     * @param entity which extends BaseEntity<ID>
     * @return Element
     */
    @Override
    protected Element convertEntity(Food entity) {
        Element foodElement = rootDocument.createElement("food");
        foodElement.setAttribute("id", entity.getId().toString());
        addChildWithTextContent(foodElement, "name", entity.getName());
        addChildWithTextContent(foodElement, "producer", entity.getProducer());
        Date date = entity.getExpirationDate();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        addChildWithTextContent(foodElement, "expirationDate", dateFormat.format(date));
        return foodElement;
    }
}
