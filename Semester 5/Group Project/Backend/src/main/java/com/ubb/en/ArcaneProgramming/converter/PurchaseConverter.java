package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.PurchaseDto;
import com.ubb.en.ArcaneProgramming.model.Purchase;

public class PurchaseConverter {
    public static PurchaseDto convertToDto(Purchase purchase) {
        return new PurchaseDto(
                purchase.getID(),
                purchase.getDate(),
                purchase.getPrice(),
                ArcaneUserConverter.convertToDto(purchase.getArcaneUser()),
                GameConverter.convertToDto(purchase.getGame())
        );
    }

    public static Purchase convertToModel(PurchaseDto purchaseDto) {
        return new Purchase(
                purchaseDto.getID(),
                purchaseDto.getDate(),
                purchaseDto.getPrice(),
                ArcaneUserConverter.convertToModel(purchaseDto.getArcaneUserDto()),
                GameConverter.convertToModel(purchaseDto.getGameDto())
        );
    }
}
