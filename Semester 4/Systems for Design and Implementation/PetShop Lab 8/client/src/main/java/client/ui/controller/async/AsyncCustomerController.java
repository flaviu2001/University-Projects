package client.ui.controller.async;

import core.domain.Customer;
import core.exceptions.PetShopException;
import core.service.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import web.dto.CustomerDTO;
import web.dto.CustomersDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class AsyncCustomerController {
    public static final Logger logger = LoggerFactory.getLogger(AsyncCatController.class);
    @Autowired
    ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    public CompletableFuture<Iterable<Customer>> getCustomersFromRepository(){
        logger.trace("getCustomerFromRepository - method entered");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/customers";
                CustomersDTO customers = restTemplate.getForObject(url, CustomersDTO.class);
                if (customers == null)
                    throw new PetShopException("Could not retrieve customers from server");
                return customers.getCustomers()
                        .stream()
                        .map(customerDTO -> new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhoneNumber()))
                        .collect(Collectors.toSet());
            }catch (ResourceAccessException resourceAccessException){
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

    public CompletableFuture<String> addCustomer(String name, String phoneNumber){
        logger.trace("addCustomer - method entered " + name + ", " + phoneNumber);
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/customers";
                restTemplate.postForObject(url,
                        new CustomerDTO(name, phoneNumber),
                        CustomerDTO.class);
                return "Customer added";
            }catch (ResourceAccessException resourceAccessException) {
                return "Inaccessible server";
            }
        }, executorService);
    }

    public CompletableFuture<String> deleteCustomer(Long id){
        return CompletableFuture.supplyAsync(()->{
            try{
                String url = "http://localhost:8080/api/customers";
                restTemplate.delete(url + "/{id}", id);
                return "Customer deleted";
            }catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }

    public CompletableFuture<String> updateCustomer(Long id, String name, String phoneNumber){
        return CompletableFuture.supplyAsync(()->{
            try{
                String url = "http://localhost:8080/api/customers";
                CustomerDTO customerToUpdate = new CustomerDTO(name, phoneNumber);
                customerToUpdate.setId(id);
                restTemplate.put(url + "/{id}", customerToUpdate, customerToUpdate.getId());
                return "Customer updated";
            }catch (ResourceAccessException resourceAccessException) {
                throw new PetShopException("Inaccessible server");
            }
        }, executorService);
    }
}
