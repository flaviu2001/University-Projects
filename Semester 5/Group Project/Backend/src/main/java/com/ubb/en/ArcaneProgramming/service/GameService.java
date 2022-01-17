package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.Game;
import com.ubb.en.ArcaneProgramming.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public void addGame(Game game) {
        gameRepository.save(game);
    }

    public Game findGame(Long gameID) {
        return gameRepository.findById(gameID).orElseThrow(() -> new RuntimeException("no game with this id"));
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public void updateGame(Game game) {
        gameRepository.save(game);
    }

    public void deleteGame(Long gameID) {
        gameRepository.deleteById(gameID);
    }

    public List<Game> getGamesOwnedByUser(String name) {
        return gameRepository.getGamesOwnedByUser(name);
    }

    public Game findGameByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public List<Game> getWishList(String username) {
        return gameRepository.getWishList(username);
    }
}