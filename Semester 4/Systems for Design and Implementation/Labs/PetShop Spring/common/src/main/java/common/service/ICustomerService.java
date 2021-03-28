package common.service;

import common.domain.Customer;
import common.domain.Pair;
import common.exceptions.PetShopException;
import common.exceptions.ValidatorException;

import java.util.List;
import java.util.Set;

public interface ICustomerService {
    /**
     * Saves the customer with the given attributes to the repository of customers
     *
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the cat entity is not valid.
     */
    void addCustomer(String name, String phoneNumber);

    /**
     * @return all customers from the repository
     */
    Set<Customer> getCustomersFromRepository();

    /**
     * Deletes a customer based on it's id
     *
     * @param id - id of the customer to be deleted
     * @throws IllegalArgumentException if the given id is null.
     * @throws PetShopException         if the customer does not exist
     */
    void deleteCustomer(Long id);

    /**
     * @param id must not be null
     * @throws IllegalArgumentException if the given id is null.
     * @throws ValidatorException       if the customer entity is not valid.
     * @throws PetShopException         if the customer does not exist
     */
    void updateCustomer(Long id, String name, String phoneNumber);
}
