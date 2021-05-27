package client.service;

import common.domain.Customer;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;
import common.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class CustomerServiceClientImpl implements ICustomerService {
    @Autowired
    private ICustomerService customerService;

    /**
     * Saves the customer with the given attributes to the repository of customers
     *
     * @param name name of new customer
     * @param phoneNumber phone of new customer
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */
    @Override
    public void addCustomer(String name, String phoneNumber) {
        customerService.addCustomer(name, phoneNumber);
    }

    /**
     * @return all customers from the repository
     */
    @Override
    public Set<Customer> getCustomersFromRepository() {
        return customerService.getCustomersFromRepository();
    }

    /**
     * Deletes a customer based on it's id
     *
     * @param id - id of the customer to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the customer does not exist
     */
    @Override
    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
    }

    /**
     * @param id          must not be null
     * @param name new name of customer
     * @param phoneNumber new phone of customer
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the customer entity is not valid.
     * @throws PetShopException         if the customer does not exist
     */
    @Override
    public void updateCustomer(Long id, String name, String phoneNumber) {
        customerService.updateCustomer(id, name, phoneNumber);
    }
}
