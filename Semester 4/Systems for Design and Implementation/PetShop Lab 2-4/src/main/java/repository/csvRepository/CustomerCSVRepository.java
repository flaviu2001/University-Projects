package repository.csvRepository;

import domain.Customer;
import domain.validators.Validator;

import java.util.Arrays;
import java.util.List;

public class CustomerCSVRepository extends CSVRepository<Long, Customer> {

    public CustomerCSVRepository(Validator<Customer> validator, String filePath) {
        super(validator, filePath);
    }

    /**
     * Extracts the customer from a line in the CSV file
     * @param dataTransferObject is the intermediary between data source and the program
     * @return the extracted Customer
     */
    @Override
    protected Customer extractEntity(String dataTransferObject) {
        List<String> tokens = Arrays.asList(dataTransferObject.split(","));
        Long id = Long.parseLong(tokens.get(0));
        String name = tokens.get(1);
        String phoneNumber = tokens.get(2);
        return new Customer(id, name, phoneNumber);
    }

    /**
     * Maps the given customer to a string in csv format
     * @param customer customer to be mapped to a string
     * @return string in csv format containing the Customer's attributes
     */
    @Override
    protected String convertEntity(Customer customer) {
        return customer.getId() + "," +
                customer.getName() + "," +
                customer.getPhoneNumber();
    }
}
