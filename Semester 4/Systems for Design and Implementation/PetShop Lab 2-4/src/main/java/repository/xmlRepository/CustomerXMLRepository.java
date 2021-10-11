package repository.xmlRepository;

import domain.Customer;
import domain.validators.Validator;
import org.w3c.dom.Element;

public class CustomerXMLRepository extends XMLRepository<Long, Customer> {

    public CustomerXMLRepository(Validator<Customer> validator, String filePath) {
        super(validator, filePath, "customers");
    }

    /**
     * Extracts the Customer object from Element
     * @param dataTransferObject is the intermediary between data source and the program
     * @return newly created customer
     */
    @Override
    protected Customer extractEntity(Element dataTransferObject) {
        Long id = Long.parseLong(dataTransferObject.getAttribute("id"));
        String name = dataTransferObject.getElementsByTagName("name").item(0).getTextContent();
        String phoneNumber = dataTransferObject.getElementsByTagName("phoneNumber").item(0).getTextContent();
        return new Customer(id, name, phoneNumber);
    }

    /**
     * Gets the Element object from Customer
     * @param customer that extends BaseEntity<ID>
     * @return Element
     */
    @Override
    protected Element convertEntity(Customer customer) {
        Element customerElement = rootDocument.createElement("customer");
        customerElement.setAttribute("id", customer.getId().toString());
        addChildWithTextContent(customerElement, "name", customer.getName());
        addChildWithTextContent(customerElement, "phoneNumber", customer.getPhoneNumber());
        return customerElement;
    }
}
