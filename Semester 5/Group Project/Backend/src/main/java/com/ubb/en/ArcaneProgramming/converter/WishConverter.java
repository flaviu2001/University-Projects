package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.WishDto;
import com.ubb.en.ArcaneProgramming.model.Wish;

public class WishConverter {
    public static WishDto convertToDto(Wish wish) {
        return new WishDto(
                wish.getID(),
                ArcaneUserConverter.convertToDto(wish.getArcaneUser()),
                GameConverter.convertToDto(wish.getGame())
        );
    }

    public static Wish convertToModel(WishDto wishDto) {
        return new Wish(
                wishDto.getID(),
                ArcaneUserConverter.convertToModel(wishDto.getArcaneUserDto()),
                GameConverter.convertToModel(wishDto.getGameDto())
        );
    }
}
