package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.PurchaseConverter;
import com.ubb.en.ArcaneProgramming.dto.PurchaseDto;
import com.ubb.en.ArcaneProgramming.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/")
    public ResponseEntity<List<PurchaseDto>> getAllPurchases() {
        return new ResponseEntity<>(purchaseService.getAllPurchases().stream().map(PurchaseConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDto> getPurchaseById(@PathVariable Long id) {
        return new ResponseEntity<>(PurchaseConverter.convertToDto(purchaseService.findPurchase(id)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> addPurchase(@RequestBody PurchaseDto purchaseDto) {
        purchaseService.addPurchase(PurchaseConverter.convertToModel(purchaseDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<HttpStatus> updatePurchase(@RequestBody PurchaseDto purchaseDto) {
        purchaseService.updatePurchase(PurchaseConverter.convertToModel(purchaseDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
