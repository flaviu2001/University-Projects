package repository.xmlRepository;

import domain.CatFood;
import domain.Pair;
import domain.validators.Validator;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

public class CatFoodXMLRepository extends XMLRepository<Pair<Long, Long>, CatFood> {
    public CatFoodXMLRepository(Validator<CatFood> validator, String filePath) {
        super(validator, filePath, "catFoods");
    }

    /**
     * Extracts PetFood from Element
     * @param dataTransferObject is the intermediary between data source and the program
     * @return CatFood
     */
    @Override
    protected CatFood extractEntity(Element dataTransferObject) {
        String stringID = dataTransferObject.getAttribute("id");
        List<String> idList = Arrays.asList(stringID.split(","));
        return new CatFood(Long.parseLong(idList.get(0)), Long.parseLong(idList.get(1)));
    }

    /**
     * Gets Element from CatFood entity
     * @param entity which extends BaseEntity<ID>
     * @return  Element
     */
    @Override
    protected Element convertEntity(CatFood entity) {
        Element foodElement = rootDocument.createElement("catFood");
        foodElement.setAttribute("id", entity.getCatId().toString() + "," + entity.getFoodId().toString());
        return foodElement;
    }
}
