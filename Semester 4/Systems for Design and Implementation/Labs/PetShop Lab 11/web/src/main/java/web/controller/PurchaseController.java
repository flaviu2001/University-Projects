package web.controller;


import core.domain.Purchase;
import core.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import web.converter.PurchaseConverter;
import web.dto.CustomerPurchasePrimaryKeyDTO;
import web.dto.CustomerSpentCashDTO;
import web.dto.CustomersSpentCashDTO;
import web.dto.PurchasesDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PurchaseController {
    public static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseConverter purchaseConverter;

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
}
