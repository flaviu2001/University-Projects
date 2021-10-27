package web.controller;


import core.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.converter.PurchaseConverter;
import web.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PurchaseController {
    public static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseConverter purchaseConverter;

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

    @RequestMapping(value = "/purchases-of-cat/{id}")
    PurchasesDTO getPurchasesOfCat (@PathVariable Long id) {
        return new PurchasesDTO(purchaseConverter.convertModelsToDTOs(purchaseService.findPurchasesOfCatByCatId(id)));
    }

    @RequestMapping(value = "/purchases-of-customer/{id}")
    PurchasesDTO getPurchasesOfCustomer(@PathVariable Long id) {
        return new PurchasesDTO(purchaseConverter.convertModelsToDTOs(purchaseService.findPurchasesOfCustomerByCustomerId(id)));
    }

    @RequestMapping(value = "/sortedCustomers")
    CustomersSpentCashDTO getSortedCustomers(){
        logger.trace("getCustomersThatBoughtBreed - method entered");
        List<CustomerSpentCashDTO> customers = purchaseService.getPurchasesSortedSpentCashInterface().stream()
                .map(pair-> new CustomerSpentCashDTO(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
        CustomersSpentCashDTO customersDTO = new CustomersSpentCashDTO(customers);
        logger.trace("getCustomersThatBoughtBreed: " + customers);
        return customersDTO;
    }
}
