package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.GameConverter;
import com.ubb.en.ArcaneProgramming.dto.GameDto;
import com.ubb.en.ArcaneProgramming.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
}
