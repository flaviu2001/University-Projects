package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r join r.game g where g.title = :title")
    List<Review> getReviewsOfGame(@Param("title") String title);

    @Query("select r from Review r join r.arcaneUser a where a.userName = :username")
    List<Review> getReviewsOfUsername (@Param("username") String username);

    @Query("select r from Review r join r.game g join r.arcaneUser a where g.title = :title and a.userName = :username")
    List<Review> getReviewsOfGameAndUsername(@Param("title") String title, @Param("username") String username);
}
