package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.WishConverter;
import com.ubb.en.ArcaneProgramming.dto.WishDto;
import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.model.Game;
import com.ubb.en.ArcaneProgramming.model.Wish;
import com.ubb.en.ArcaneProgramming.service.GameService;
import com.ubb.en.ArcaneProgramming.service.UserService;
import com.ubb.en.ArcaneProgramming.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wish")
public class WishController {
    @Autowired
    private WishService wishService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @GetMapping("/getWishesForUser/{username}")
    public ResponseEntity<List<WishDto>> getAllWishes(@PathVariable String username) {
        ArcaneUser user = userService.findUserByUserName(username);
        return new ResponseEntity<>(wishService.getAllWishesForUser(user).stream().map(WishConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<WishDto> addWish(@RequestBody WishDto wishDto) {
        Wish wish = new Wish();
        wish.setArcaneUser(userService.findUserByUserName(wishDto.getArcaneUserName()));
        wish.setGame(gameService.findGame(wishDto.getGameID()));
        wish = wishService.addWish(wish);
        return new ResponseEntity<>(WishConverter.convertToDto(wish), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{gameID}/{username}")
    public ResponseEntity<HttpStatus> deleteWish(@PathVariable Long gameID, @PathVariable String username) {
        ArcaneUser arcaneUser = userService.findUserByUserName(username);
        Game game = gameService.findGame(gameID);
        wishService.deleteWish(game, arcaneUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
