package core.service;

import core.domain.Customer;
import core.exceptions.PetShopException;
import core.repository.ICustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Math.max;

@Service
public class CustomerServiceImpl implements ICustomerService {
    public static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public void addCustomer(String name, String phoneNumber) {
        logger.trace("addCustomer - method entered - name: " + name + ", phoneNumber: " + phoneNumber);
        long id = 0;
        for (Customer customer : this.customerRepository.findAll())
            id = max(id, customer.getId() + 1);
        Customer customerToBeAdded = new Customer(id, name, phoneNumber);
        customerRepository.save(customerToBeAdded);
        logger.trace("addCustomer - method finished");

    }

    @Override
    public List<Customer> getCustomersFromRepository() {
        logger.trace("getCustomersFromRepository - method entered");
        List<Customer> customers = customerRepository.findAll();
        logger.trace("getCustomersFromRepository: " + customers.toString());
        return customers;

    }


    @Override
    public void deleteCustomer(Long id) {
        logger.trace("deleteCustomer - method entered - id: " + id);

        customerRepository.findById(id)
                .ifPresentOrElse((customer) -> customerRepository.deleteById(customer.getId()),
                        () -> {
                            throw new PetShopException("Customer does not exist");
                        });
        logger.trace("deleteCustomer - method finished");

    }

    @Override
    @Transactional
    public void updateCustomer(Long id, String name, String phoneNumber) {
        logger.trace("updateCustomer - method entered - id: " + id + ", name: " + name + ", phoneNumber: " + phoneNumber);

        customerRepository.findById(id)
                .ifPresentOrElse(
                        (customer) -> {
                            customer.setName(name);
                            customer.setPhoneNumber(phoneNumber);
                        },
                        () -> {
                            throw new PetShopException("Customer does not exist");
                        }
                );

        logger.trace("updateCustomer - method finished");
    }
}
