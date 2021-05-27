package web.controller;

import core.domain.Customer;
import core.service.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.CustomerConverter;
import web.dto.CustomerDTO;
import web.dto.CustomersDTO;

import java.util.List;

@RestController
public class CustomerController {
    public static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private CustomerConverter customerConverter;

    @RequestMapping(value = "/customers")
    CustomersDTO getCustomersFromRepository(){
        logger.trace("getAllCustomers - method entered");
        List<Customer> customers = customerService.getCustomersFromRepository();
        CustomersDTO customersDTO = new CustomersDTO(customerConverter.convertModelsToDTOs(customers));
        logger.trace("getAllCustomers: " + customers);
        return customersDTO;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    void addCustomer(@RequestBody CustomerDTO customerDTO){
        logger.trace("addCustomer - method entered - catDTO: " + customerDTO);
        var customer = customerConverter.convertDtoToModel(customerDTO);
        customerService.addCustomer(
                customer.getName(),
                customer.getPhoneNumber()
        );

        logger.trace("addCustomer - customer added");
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    void updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        logger.trace("updateCustomer - method entered - customerDTO: " + customerDTO);
        var customer = customerConverter.convertDtoToModel(customerDTO);
        customerService.updateCustomer(
                id,
                customer.getName(),
                customer.getPhoneNumber()
        );
        logger.trace("updateCustomer - customer updated");
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
