package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.GameDto;
import com.ubb.en.ArcaneProgramming.model.Game;

public class GameConverter {
    public static GameDto convertToDto(Game game) {
        GameDto gameDto = new GameDto();
        gameDto.setDescription(game.getDescription());
        gameDto.setPrice(game.getPrice());
        gameDto.setTitle(game.getTitle());
        gameDto.setID(game.getID());
        gameDto.setPictureUrl(game.getPictureUrl());
        return gameDto;
    }

    public static Game convertToModel(GameDto gameDto) {
        return new Game(gameDto.getID(), gameDto.getTitle(), gameDto.getDescription(), gameDto.getPrice(), gameDto.getPictureUrl());
    }
}
