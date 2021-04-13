package web.controller;


import core.domain.CatFood;
import core.domain.CustomerPurchasePrimaryKey;
import core.domain.Purchase;
import core.service.IPurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.converter.PurchaseConverter;
import web.dto.*;

import java.util.List;

@RestController
public class PurchaseController {
    public static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private IPurchaseService purchaseService;

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
}
