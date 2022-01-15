package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.Purchase;
import com.ubb.en.ArcaneProgramming.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public void addPurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    public Purchase findPurchase(Long purchaseID) {
        return purchaseRepository.findById(purchaseID).orElseThrow(() -> new RuntimeException("no purchase with this id"));
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public void updatePurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    public void deletePurchase(Long purchaseID) {
        purchaseRepository.deleteById(purchaseID);
    }
}
