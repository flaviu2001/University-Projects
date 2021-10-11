package web.controller;


import core.domain.*;
import core.service.IPurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.CustomerConverter;
import web.converter.PurchaseConverter;
import web.dto.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PurchaseController {
    public static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private PurchaseConverter purchaseConverter;

    @Autowired
    private CustomerConverter customerConverter;

    @RequestMapping(value = "/purchases")
    PurchasesDTO getPurchasesFromRepository(){
        logger.trace("getPurchasesFromRepository - method entered");
        List<Purchase> purchases = purchaseService.getPurchasesFromRepository();
        PurchasesDTO purchasesDTO = new PurchasesDTO(purchaseConverter.convertModelsToDTOs(purchases));
        logger.trace("getPurchasesFromRepository: " + purchases);
        return purchasesDTO;
    }

    @RequestMapping(value = "/purchases", method = RequestMethod.POST)
    void addPurchase(@RequestBody CustomerPurchasePrimaryKeyDTO customerPurchasePrimaryKeyDTO){
        logger.trace("addPurchase - method entered - customerPurchasePrimaryKeyDTO: " + customerPurchasePrimaryKeyDTO);
        purchaseService.addPurchase(
                customerPurchasePrimaryKeyDTO.getCatId(),
                customerPurchasePrimaryKeyDTO.getCustomerId(),
                customerPurchasePrimaryKeyDTO.getPrice(),
                customerPurchasePrimaryKeyDTO.getDateAcquired(),
                customerPurchasePrimaryKeyDTO.getReview()
                );
        logger.trace("addPurchase - purchase added");
    }

    @RequestMapping(value = "/purchases/{newReview}", method = RequestMethod.PUT)
    void updatePurchase(@PathVariable int newReview, @RequestBody SimplePurchasePrimaryKeyDTO simplePurchasePrimaryKeyDTO){
        logger.trace("updatePurchase - method entered - simplePurchasePrimaryKeyDTO: " + simplePurchasePrimaryKeyDTO);
        purchaseService.updatePurchase(simplePurchasePrimaryKeyDTO.getCatId(), simplePurchasePrimaryKeyDTO.getCustomerId(), newReview);
        logger.trace("updatePurchase - purchase updated");
    }

    @RequestMapping(value = "/purchases/{catId}&{customerId}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCatFood(@PathVariable Long catId, @PathVariable Long customerId) {
        purchaseService.deletePurchase(catId, customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/purchases/breed={breed}")
    CustomersDTO getCustomersThatBoughtBreed(@PathVariable String breed){
        logger.trace("getCustomersThatBoughtBreed - method entered");
        Set<Customer> customers = purchaseService.filterCustomersThatBoughtBreedOfCat(breed);
        CustomersDTO purchasesDTO = new CustomersDTO(customerConverter.convertModelsToDTOs(customers));
        logger.trace("getCustomersThatBoughtBreed: " + customers);
        return purchasesDTO;
    }

    @RequestMapping(value = "/sortedCustomers")
    CustomersSpentCashDTO getSortedCustomers(){
        logger.trace("getCustomersThatBoughtBreed - method entered");
        List<CustomerSpentCashDTO> customers = purchaseService.reportCustomersSortedBySpentCash().stream()
                .map(pair-> new CustomerSpentCashDTO(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
        CustomersSpentCashDTO customersDTO = new CustomersSpentCashDTO(customers);
        logger.trace("getCustomersThatBoughtBreed: " + customers);
        return customersDTO;
    }

    @RequestMapping(value = "/purchases/minReview={minReview}")
    PurchasesDTO getPurchasesWithMinReview(@PathVariable Integer minReview){
        logger.trace("getPurchasesWithMinReview - method entered");
        List<Purchase> purchases = purchaseService.filterPurchasesWithMinStars(minReview);
        PurchasesDTO purchasesDTO = new PurchasesDTO(purchaseConverter.convertModelsToDTOs(purchases));
        logger.trace("getPurchasesWithMinReview: " + purchases);
        return purchasesDTO;
    }
}
