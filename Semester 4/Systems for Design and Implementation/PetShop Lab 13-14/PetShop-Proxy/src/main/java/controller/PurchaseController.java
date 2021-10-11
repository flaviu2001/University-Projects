package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class PurchaseController {
    private final String purchaseUrl = "http://purchases:8080/api";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/purchases", method = RequestMethod.POST)
    void addPurchase(@RequestBody Object purchase){
        restTemplate.postForObject(purchaseUrl + "/purchases", purchase, Object.class);
    }

    @RequestMapping(value = "/purchases-of-cat/{id}")
    Object getPurchasesOfCat (@PathVariable Long id) {
        return restTemplate.getForObject(purchaseUrl + "/purchases-of-cat/" + id, Object.class);
    }

    @RequestMapping(value = "/purchases-of-customer/{id}")
    Object getPurchasesOfCustomer(@PathVariable Long id) {
        return restTemplate.getForObject(purchaseUrl + "/purchases-of-customer/" + id, Object.class);
    }

    @RequestMapping(value = "/sortedCustomers")
    Object getSortedCustomers(){
        return restTemplate.getForObject(purchaseUrl + "/sortedCustomers", Object.class);
    }
}
