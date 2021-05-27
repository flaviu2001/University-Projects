package repository.xmlRepository;

import domain.Cat;
import domain.validators.Validator;
import org.w3c.dom.Element;

public class CatXMLRepository extends XMLRepository<Long, Cat> {
    public CatXMLRepository(Validator<Cat> validator, String filePath) {
        super(validator, filePath, "cats");
    }

    /**
     *  Extracts Cat object from Element
     * @param dataTransferObject is the intermediary between data source and the program
     * @return newly created cat
     */
    @Override
    protected Cat extractEntity(Element dataTransferObject) {
        Long id = Long.parseLong(dataTransferObject.getAttribute("id"));
        String name = dataTransferObject.getElementsByTagName("name").item(0).getTextContent();
        String breed = dataTransferObject.getElementsByTagName("breed").item(0).getTextContent();
        int years = Integer.parseInt(dataTransferObject.getElementsByTagName("age").item(0).getTextContent());
        return new Cat(id, name, breed, years);
    }

    /**
     * Gets Element from Cat
     * @param entity which extends BaseEntity<ID>
     * @return Element
     */
    @Override
    protected Element convertEntity(Cat entity) {
        Element catElement = rootDocument.createElement("cat");
        catElement.setAttribute("id", entity.getId().toString());
        addChildWithTextContent(catElement, "name", entity.getName());
        addChildWithTextContent(catElement, "breed", entity.getBreed());
        addChildWithTextContent(catElement, "age", entity.getCatYears().toString());
        return catElement;
    }

}
