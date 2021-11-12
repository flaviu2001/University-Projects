package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.WishConverter;
import com.ubb.en.ArcaneProgramming.dto.WishDto;
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

    @GetMapping("/")
    public ResponseEntity<List<WishDto>> getAllWishs() {
        return new ResponseEntity<>(wishService.getAllWishes().stream().map(WishConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishDto> getWishById(@PathVariable Long id) {
        return new ResponseEntity<>(WishConverter.convertToDto(wishService.findWish(id)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> addWish(@RequestBody WishDto wishDto) {
        wishService.addWish(WishConverter.convertToModel(wishDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<HttpStatus> updateWish(@RequestBody WishDto wishDto) {
        wishService.updateWish(WishConverter.convertToModel(wishDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWish(@PathVariable Long id) {
        wishService.deleteWish(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
