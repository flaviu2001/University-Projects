package service;

import common.domain.Customer;
import common.exceptions.PetShopException;
import common.service.ICustomerService;
import repository.databaseRepository.CustomerDatabaseRepository;

import java.util.Set;

import static java.lang.Math.max;

public class CustomerServiceServerImpl implements ICustomerService {
    final CustomerDatabaseRepository customerRepository;

    public CustomerServiceServerImpl(CustomerDatabaseRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomer(String name, String phoneNumber) {
        long id = 0;
        for (Customer customer:this.customerRepository.findAllEntities())
            id = max(id, customer.getId()+1);
        Customer customerToBeAdded = new Customer(id, name, phoneNumber);
        customerRepository.saveEntity(customerToBeAdded);
    }

    @Override
    public Set<Customer> getCustomersFromRepository() {
        return (Set<Customer>) customerRepository.findAllEntities();
    }


    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteEntity(id).orElseThrow(() -> {
            throw new PetShopException("Customer does not exist");
        });
    }

    @Override
    public void updateCustomer(Long id, String name, String phoneNumber) {
        customerRepository.update(new Customer(id, name, phoneNumber))
                .orElseThrow(() -> new PetShopException("Customer does not exist"));
    }
}
