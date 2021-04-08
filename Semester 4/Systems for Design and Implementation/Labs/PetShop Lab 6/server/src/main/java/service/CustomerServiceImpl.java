package service;

import common.domain.Customer;
import common.exceptions.PetShopException;
import common.service.ICustomerService;
import repository.IRepository;

import java.util.Set;

import static java.lang.Math.max;

public class CustomerServiceImpl implements ICustomerService {
    final IRepository<Long, Customer> customerRepository;

    public CustomerServiceImpl(IRepository<Long, Customer> customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomer(String name, String phoneNumber) {
        long id = 0;
        for (Customer customer:this.customerRepository.findAll())
            id = max(id, customer.getId()+1);
        Customer customerToBeAdded = new Customer(id, name, phoneNumber);
        customerRepository.save(customerToBeAdded);
    }

    @Override
    public Set<Customer> getCustomersFromRepository() {
        return (Set<Customer>) customerRepository.findAll();
    }


    @Override
    public void deleteCustomer(Long id) {
        customerRepository.delete(id).orElseThrow(() -> {
            throw new PetShopException("Customer does not exist");
        });
    }

    @Override
    public void updateCustomer(Long id, String name, String phoneNumber) {
        customerRepository.update(new Customer(id, name, phoneNumber))
                .orElseThrow(() -> new PetShopException("Customer does not exist"));
    }
}
