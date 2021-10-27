package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class CustomerController {
    private final String customerUrl = "http://customers:3000";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/customers")
    Object getCustomers() {
        return restTemplate.getForObject(customerUrl + "/customers", Object.class);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    void addCustomer(@RequestBody Object customer) {
        restTemplate.postForObject(customerUrl + "/customers", customer, Object.class);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.PUT)
    void updateCustomer(@RequestBody Object customer) {
        restTemplate.put(customerUrl + "/customers", customer);
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    void deleteCustomer(@PathVariable Long id) {
        restTemplate.delete(customerUrl + "/customers/" + id);
    }
}
