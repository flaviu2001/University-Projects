package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import com.ubb.en.ArcaneProgramming.model.Game;
import com.ubb.en.ArcaneProgramming.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByArcaneUser(ArcaneUser user);

    Wish findByGameAndArcaneUser(Game game, ArcaneUser arcaneUser);
}
