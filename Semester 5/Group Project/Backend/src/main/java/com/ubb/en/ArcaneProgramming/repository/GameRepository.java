package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("select distinct g from Purchase p join p.game g join p.arcaneUser u where u.userName = :name")
    List<Game> getGamesOwnedByUser(@Param("name")String name);

    public Game findByTitle(String title);
}
