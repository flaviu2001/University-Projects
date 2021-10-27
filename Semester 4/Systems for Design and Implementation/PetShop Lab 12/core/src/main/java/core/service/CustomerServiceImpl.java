package core.service;

import core.domain.Customer;
import core.domain.PetHouse;
import core.domain.Purchase;
import core.exceptions.PetShopException;
import core.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Math.max;

@Service
public class CustomerServiceImpl implements CustomerService {
    public static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Value("${method}")
    private String method;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void addCustomer(String name, String phoneNumber, PetHouse petHouse) {
        logger.trace("addCustomer - method entered - name: " + name + ", phoneNumber: " + phoneNumber);
        long id = 0;
        for (Customer customer : this.customerRepository.findAll())
            id = max(id, customer.getId() + 1);
        Customer customerToBeAdded = new Customer(id, name, phoneNumber);
        if (petHouse != null)
            customerToBeAdded.setPetHouse(petHouse);
        customerRepository.save(customerToBeAdded);
        logger.trace("addCustomer - method finished");

    }

    @Override
    public List<Customer> getCustomersFromRepository() {
        logger.trace("getCustomersFromRepository - method entered");
        List<Customer> customers = customerRepository.findAll();
        logger.trace("getCustomersFromRepository: " + customers);
        return customers;

    }

    @Override
    public List<Purchase> findPurchasesOfCustomerByCustomerId(long customerId) {
        System.out.println(method);
        switch (method) {
            case "jpql": {
                return customerRepository.findPurchasesOfCustomerByCustomerIdJPQL(customerId);
            }
            case "criteria": {
                return customerRepository.findPurchasesOfCustomerByCustomerIdCriteria(customerId);
            }
            case "native": {
                return customerRepository.findPurchasesOfCustomerByCustomerIdNative(customerId);
            }
            default: throw new PetShopException("invalid setting");
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        logger.trace("deleteCustomer - method entered - id: " + id);

        customerRepository.findById(id)
                .ifPresent((customer) -> customerRepository.deleteById(customer.getId()));
        logger.trace("deleteCustomer - method finished");

    }

    @Override
    @Transactional
    public void updateCustomer(Long id, String name, String phoneNumber, PetHouse petHouse) {
        logger.trace("updateCustomer - method entered - id: " + id + ", name: " + name + ", phoneNumber: " + phoneNumber);

        customerRepository.findById(id)
                .ifPresent(
                        (customer) -> {
                            customer.setName(name);
                            customer.setPhoneNumber(phoneNumber);
                            customer.setPetHouse(petHouse);
                        }
                );

        logger.trace("updateCustomer - method finished");
    }
}
