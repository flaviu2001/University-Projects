package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
