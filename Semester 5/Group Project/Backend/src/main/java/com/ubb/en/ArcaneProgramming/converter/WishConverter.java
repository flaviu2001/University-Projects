package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.WishDto;
import com.ubb.en.ArcaneProgramming.model.Wish;

public class WishConverter {
    public static WishDto convertToDto(Wish wish) {
        return new WishDto(
                wish.getID(),
                wish.getArcaneUser().getUserName(),
                wish.getGame().getID()
        );
    }
}
