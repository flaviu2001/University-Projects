package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.Wish;
import com.ubb.en.ArcaneProgramming.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    @Autowired
    private WishRepository wishRepository;

    public Wish addWish(Wish wish) {
        return wishRepository.save(wish);
    }

    public Wish findWish(Long wishID) {
        return wishRepository.findById(wishID).orElseThrow(() -> new RuntimeException("no wish with this id"));
    }

    public List<Wish> getAllWishes() {
        return wishRepository.findAll();
    }

    public Wish updateWish(Wish wish) {
        return wishRepository.save(wish);
    }

    public void deleteWish(Long wishID) {
        wishRepository.deleteById(wishID);
    }
}
