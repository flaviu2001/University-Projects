package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.ArcaneUserConverter;
import com.ubb.en.ArcaneProgramming.converter.GameConverter;
import com.ubb.en.ArcaneProgramming.dto.ArcaneUserDto;
import com.ubb.en.ArcaneProgramming.dto.GameDto;
import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.model.Game;
import com.ubb.en.ArcaneProgramming.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public ResponseEntity<List<GameDto>> getAllGames() {
        return new ResponseEntity<>(gameService.getAllGames().stream().map(GameConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable Long id) {
        return new ResponseEntity<>(GameConverter.convertToDto(gameService.findGame(id)), HttpStatus.OK);
    }


    @GetMapping("/fromUser/{name}")
    public ResponseEntity<List<GameDto>> getGamesFromUser(@PathVariable String name) {
        return new ResponseEntity<>(gameService.getGamesOwnedByUser(name).stream().map(GameConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getWishList/{username}")
    public ResponseEntity<List<GameDto>> getWishList(@PathVariable String username){
        return new ResponseEntity<>(gameService.getWishList(username).stream().map(GameConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getGameByTitle/{title}")
    public ResponseEntity<GameDto> getGameByTitle(@PathVariable String title) {
        return new ResponseEntity<>(GameConverter.convertToDto(gameService.findGameByTitle(title)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> addGame(@RequestBody GameDto gameDto) {
        gameService.addGame(GameConverter.convertToModel(gameDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<HttpStatus> updateGame(@RequestBody GameDto gameDto) {
        gameService.updateGame(GameConverter.convertToModel(gameDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = {"/searchGames/{game}", "/searchGames"})
    public ResponseEntity<List<GameDto>> searchGame(@PathVariable(required = false) String game) {
        List<GameDto> games = new ArrayList<>();

        if(game == null || game.isEmpty()) game = "";
        game = game.toLowerCase(Locale.ROOT);

        for(Game g: gameService.getAllGames()) {
            if(g.getTitle().toLowerCase(Locale.ROOT).contains(game)) {
                games.add(GameConverter.convertToDto(g));
            }
        }

        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
